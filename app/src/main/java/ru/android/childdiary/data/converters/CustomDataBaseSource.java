package ru.android.childdiary.data.converters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import io.requery.android.sqlite.DatabaseSource;
import io.requery.meta.EntityModel;
import io.requery.sql.Mapping;
import io.requery.sql.Platform;
import io.requery.sql.platform.SQLite;

public class CustomDatabaseSource extends DatabaseSource {
    public CustomDatabaseSource(Context context, EntityModel model, int version) {
        super(context, model, version);
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, int version) {
        super(context, model, name, version);
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, model, name, factory, version);
    }

    public CustomDatabaseSource(Context context, EntityModel model, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, SQLite platform) {
        super(context, model, name, factory, version, platform);
    }

    @Override
    protected Mapping onCreateMapping(Platform platform) {
        return new CustomMapping(platform);
    }
}
