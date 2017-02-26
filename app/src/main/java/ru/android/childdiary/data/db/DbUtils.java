package ru.android.childdiary.data.db;

import android.content.Context;
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
import lombok.val;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.entities.child.Models;

public class DbUtils {
    public static ReactiveEntityStore<Persistable> getDataStore(Context appContext) {
        return getDataStore(appContext, BuildConfig.DB_NAME, BuildConfig.DB_VERSION);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static ReactiveEntityStore<Persistable> getDataStore(Context appContext, String dbName, int dbVersion) {
        val source = new CustomDatabaseSource(appContext, Models.DEFAULT, dbName, dbVersion);
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

    public static <T, E> Observable<List<T>> mapReactiveResultToListObservable(ReactiveResult<E> reactiveResult, @NonNull Function<E, T> map) {
        return reactiveResult
                .observable()
                .map(map)
                .toList()
                .toObservable();
    }

    public static <T, E> Observable<T> mapReactiveResultToObservable(ReactiveResult<E> reactiveResult, @NonNull Function<E, T> map) {
        return reactiveResult
                .observable()
                .map(map)
                .firstOrError()
                .toObservable();
    }
}
