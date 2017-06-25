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
        fillTableWithValues(db, R.array.food, "food", "name");
        fillTableWithValues(db, R.array.food_measure, "food_measure", "name");
        fillTableWithValues(db, R.array.doctor, "doctor", "name");
        fillTableWithValues(db, R.array.medicine, "medicine", "name");
        fillTableWithValues(db, R.array.medicine_measure, "medicine_measure", "name");
        logger.debug("onCreate: finish inserting");
    }

    private void fillTableWithValues(SQLiteDatabase db, @ArrayRes int arrayResId, String table, String column) {
        String[] values = context.getResources().getStringArray(arrayResId);
        for (String value : values) {
            db.execSQL("insert into " + table + " (" + column + ") values ('" + value + "');");
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
