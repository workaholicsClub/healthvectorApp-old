package ru.android.childdiary.data.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.requery.BlockingEntityStore;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import lombok.val;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.entities.Models;

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
        return getDataStore(appContext, BuildConfig.DB_NAME, BuildConfig.DB_VERSION, false);
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

    public static <T, E> Observable<T> deleteObservable(EntityStore dataStore, Class<E> entityClass, T object, long objectId) {
        return Observable.fromCallable(() -> delete(dataStore, entityClass, object, objectId));
    }

    private static <T, E> T delete(EntityStore dataStore, Class<E> entityClass, T object, long objectId) {
        BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
        Object entity = blockingEntityStore.findByKey(entityClass, objectId);
        if (entity != null) {
            blockingEntityStore.delete(entity);
            return object;
        }
        throw new RuntimeException(object.getClass() + " not found while deleting");
    }

    public static <T, E> Observable<T> updateObservable(EntityStore dataStore, Class<E> entityClass, T object, long objectId,
                                                        @NonNull BiFunction<E, T, E> copy,
                                                        @NonNull Function<E, T> map) {
        return Observable.fromCallable(() -> update(dataStore, entityClass, object, objectId, copy, map));
    }

    private static <T, E> T update(EntityStore dataStore, Class<E> entityClass, T object, long objectId,
                                   @NonNull BiFunction<E, T, E> copy,
                                   @NonNull Function<E, T> map) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            E childEntity = (E) blockingEntityStore.findByKey(entityClass, objectId);
            if (childEntity != null) {
                childEntity = copy.apply(childEntity, object);
                childEntity = (E) blockingEntityStore.update(childEntity);
                return map.apply(childEntity);
            }
        } catch (Exception e) {
            throw new RuntimeException("error while updating " + object, e);
        }
        throw new RuntimeException(object + " not found while updating");
    }

    public static <T, E> Observable<List<T>> mapReactiveResult(ReactiveResult<E> reactiveResult, @NonNull Function<E, T> map) {
        return reactiveResult
                .observable()
                .map(map)
                .toList()
                .toObservable();
    }
}
