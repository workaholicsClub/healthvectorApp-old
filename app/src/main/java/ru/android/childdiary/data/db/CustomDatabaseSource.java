package ru.android.childdiary.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.requery.android.sqlite.DatabaseSource;
import io.requery.meta.EntityModel;
import io.requery.sql.Mapping;
import io.requery.sql.Platform;
import io.requery.sql.platform.SQLite;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementEntity;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.db.entities.dictionaries.FoodEntity;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineEntity;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineMeasureEntity;
import ru.android.childdiary.data.db.exceptions.DowngradeDatabaseException;
import ru.android.childdiary.domain.core.LocalizationUtils;

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

        Dictionaries dictionaries = new Dictionaries();
        int i = 0;
        for (String language : Dictionaries.LANGUAGES) {
            int index = i;
            LocalizationUtils.fillFromResources(context, language, resources -> dictionaries.fillFromResources(index, resources));
            ++i;
        }

        logger.debug("onCreate: start inserting");
        fillTableWithValues(db, FoodEntity.$TYPE.getName(), Dictionaries.FOOD_COLUMNS, dictionaries.foodNames);
        fillTableWithValues(db, FoodMeasureEntity.$TYPE.getName(), Dictionaries.FOOD_MEASURE_COLUMNS, dictionaries.foodMeasureNames);
        fillTableWithValues(db, DoctorEntity.$TYPE.getName(), Dictionaries.DOCTOR_COLUMNS, dictionaries.doctorNames);
        fillTableWithValues(db, MedicineEntity.$TYPE.getName(), Dictionaries.MEDICINE_COLUMNS, dictionaries.medicineNames);
        fillTableWithValues(db, MedicineMeasureEntity.$TYPE.getName(), Dictionaries.MEDICINE_MEASURE_COLUMNS, dictionaries.medicineMeasureNames);
        fillTableWithValues(db, AchievementEntity.$TYPE.getName(), Dictionaries.ACHIEVEMENT_COLUMNS, dictionaries.achievementNames);
        logger.debug("onCreate: finish inserting");
    }

    private void fillTableWithValues(SQLiteDatabase db, String table, String[] columns, String[][] names) {
        String columnStr = TextUtils.join(",", columns);
        int count = Integer.MAX_VALUE;
        for (String[] localizedNames : names) {
            if (localizedNames.length < count) {
                count = localizedNames.length;
            }
        }
        for (int i = 0; i < count; ++i) {
            String valuesStr = "";
            for (int j = 0; j < names.length; ++j) {
                valuesStr += "'" + names[j][i] + "'" + (j == names.length - 1 ? "" : ",");
            }
            db.execSQL("insert into " + table + " (" + columnStr + ") values (" + valuesStr + ");");
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
