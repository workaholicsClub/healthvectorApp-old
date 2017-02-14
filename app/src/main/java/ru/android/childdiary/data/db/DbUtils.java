package ru.android.childdiary.data.db;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import lombok.val;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.entities.Models;

public class DbUtils {
    public static ReactiveEntityStore<Persistable> getDataStore(Context appContext) {
        // TODO: когда структура станет стабильной, заменить последний параметр на false
        return getDataStore(appContext, BuildConfig.DB_NAME, BuildConfig.DB_VERSION, BuildConfig.DEBUG);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static ReactiveEntityStore<Persistable> getDataStore(Context appContext, String dbName, int dbVersion, boolean recreate) {
        val source = new CustomDatabaseSource(appContext, Models.DEFAULT, dbName, dbVersion);

        if (recreate) {
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }

        val configuration = source.getConfiguration();
        val dataStore = ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));
        return dataStore;
    }
}
