package ru.android.childdiary.data.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import lombok.val;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.entities.child.Models;

public class DbUtils {
    private static final int DB_VERSION_DEFAULT = 1;
    private static final String DB_VERSION_KEY = "dbVersionKey";

    private static int getDbVersion(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("dbVersion", Context.MODE_PRIVATE);
        int dbVersion = preferences.getInt(DB_VERSION_KEY, DB_VERSION_DEFAULT);
        preferences.edit().putInt(DB_VERSION_KEY, dbVersion + 1).apply();
        return dbVersion;
    }

    public static ReactiveEntityStore<Persistable> getDataStore(Context appContext) {
        // TODO: когда структура станет стабильной, заменить последний параметр на false
        return getDataStore(appContext, BuildConfig.DB_NAME, BuildConfig.DB_VERSION, BuildConfig.DEBUG);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static ReactiveEntityStore<Persistable> getDataStore(Context appContext, String dbName, int dbVersion, boolean recreate) {
        if (recreate) {
            dbVersion = getDbVersion(appContext);
        }

        val source = new CustomDatabaseSource(appContext, Models.DEFAULT, dbName, dbVersion);

        if (recreate) {
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }

        val configuration = source.getConfiguration();
        val dataStore = ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));
        return dataStore;
    }

    public static <T> Observable<T> deleteObservable(EntityStore dataStore, Class entityClass, T object, long objectId) {
        return Observable.fromCallable(() -> delete(dataStore, entityClass, object, objectId));
    }

    private static <T> T delete(EntityStore dataStore, Class entityClass, T object, long objectId) {
        BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
        Object entity = blockingEntityStore.findByKey(entityClass, objectId);
        if (entity != null) {
            blockingEntityStore.delete(entity);
            return object;
        }
        throw new RuntimeException(object.getClass().getSimpleName() + " not found while deleting");
    }
}
