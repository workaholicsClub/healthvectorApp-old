package ru.android.childdiary.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.requery.android.sqlite.DatabaseSource;
import io.requery.meta.EntityModel;
import io.requery.sql.Mapping;
import io.requery.sql.Platform;
import io.requery.sql.platform.SQLite;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementEntity;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.db.entities.dictionaries.FoodEntity;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineEntity;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineMeasureEntity;
import ru.android.childdiary.data.db.exceptions.DowngradeDatabaseException;

public class CustomDatabaseSource extends DatabaseSource {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Context context;

    public CustomDatabaseSource(Context context, EntityModel model, int version) {
        super(context, model, version);
        this.context = context;
        init();
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, int version) {
        super(context, model, name, version);
        this.context = context;
        init();
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, model, name, factory, version);
        this.context = context;
        init();
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, SQLite platform) {
        super(context, model, name, factory, version, platform);
        this.context = context;
        init();
    }

    private void init() {
        setLoggingEnabled(BuildConfig.PRINT_DB_LOGS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        logger.debug("onCreate");
        super.onCreate(db);

        // TODO: translation tables
        logger.debug("onCreate: start inserting");
        fillTableWithValues(db, R.array.food, FoodEntity.$TYPE.getName(), FoodEntity.NAME.getName());
        fillTableWithValues(db, R.array.food_measure, FoodMeasureEntity.$TYPE.getName(), FoodMeasureEntity.NAME.getName());
        fillTableWithValues(db, R.array.doctor, DoctorEntity.$TYPE.getName(), DoctorEntity.NAME.getName());
        fillTableWithValues(db, R.array.medicine, MedicineEntity.$TYPE.getName(), MedicineEntity.NAME.getName());
        fillTableWithValues(db, R.array.medicine_measure, MedicineMeasureEntity.$TYPE.getName(), MedicineMeasureEntity.NAME.getName());
        fillTableWithValues(db, R.array.achievement, AchievementEntity.$TYPE.getName(), AchievementEntity.NAME.getName());
        fillPredefinedAchievements(db);
        logger.debug("onCreate: finish inserting");
    }

    private void fillTableWithValues(SQLiteDatabase db, @ArrayRes int arrayResId, String table, String column) {
        String[] values = context.getResources().getStringArray(arrayResId);
        for (String value : values) {
            db.execSQL("insert into " + table + " (" + column + ") values ('" + value + "');");
        }
    }

    private void fillPredefinedAchievements(SQLiteDatabase db) {
        String[] values = new String[0]; // TODO context.getResources().getStringArray(R.array.achievement_predefined);
        for (int i = 0; i < values.length; ++i) {
            String value = values[i];
            db.execSQL("insert into "
                    + AchievementEntity.$TYPE.getName()
                    + " ("
                    + AchievementEntity.NAME.getName()
                    + ", "
                    + AchievementEntity.PREDEFINED.getName()
                    + ", "
                    + AchievementEntity.ORDER_NUMBER.getName()
                    + ") values ('" + value + "', 1, " + i + ");");
        }
    }

    @Override
    protected Mapping onCreateMapping(Platform platform) {
        return new CustomMapping(platform);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        logger.debug("onUpgrade: oldVersion = " + oldVersion + "; newVersion = " + newVersion);
        super.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        logger.debug("onDowngrade: oldVersion = " + oldVersion + "; newVersion = " + newVersion);
        throw new DowngradeDatabaseException("Can't downgrade database from version " + oldVersion + " to " + newVersion);
    }
}
