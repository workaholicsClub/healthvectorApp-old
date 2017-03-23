package ru.android.childdiary.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

        // TODO: translation table
        db.execSQL("insert into food_measure (name) values ('" + context.getString(R.string.food_measure_grams) + "');");
        db.execSQL("insert into food_measure (name) values ('" + context.getString(R.string.food_measure_millilitres) + "');");

        // TODO: translation table
        db.execSQL("insert into food (name) values ('" + context.getString(R.string.food_porridge) + "');");
        db.execSQL("insert into food (name) values ('" + context.getString(R.string.food_vegetables) + "');");
        db.execSQL("insert into food (name) values ('" + context.getString(R.string.food_fruits) + "');");
        db.execSQL("insert into food (name) values ('" + context.getString(R.string.food_meat) + "');");
    }

    @Override
    protected Mapping onCreateMapping(Platform platform) {
        return new CustomMapping(platform);
    }
}
