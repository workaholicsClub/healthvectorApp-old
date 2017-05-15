package ru.android.childdiary.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;

import io.requery.android.sqlite.DatabaseSource;
import io.requery.meta.EntityModel;
import io.requery.sql.Mapping;
import io.requery.sql.Platform;
import io.requery.sql.platform.SQLite;
import ru.android.childdiary.R;

public class CustomDatabaseSource extends DatabaseSource {
    private final Context context;

    public CustomDatabaseSource(Context context, EntityModel model, int version) {
        super(context, model, version);
        this.context = context;
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, int version) {
        super(context, model, name, version);
        this.context = context;
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, model, name, factory, version);
        this.context = context;
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, SQLite platform) {
        super(context, model, name, factory, version, platform);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        // TODO: translation tables
        fillTableWithValues(db, R.array.food, "food", "name");
        fillTableWithValues(db, R.array.food_measure, "food_measure", "name");
        fillTableWithValues(db, R.array.doctor, "doctor", "name");
        fillTableWithValues(db, R.array.medicine, "medicine", "name");
        fillTableWithValues(db, R.array.medicine_measure, "medicine_measure", "name");
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
}
