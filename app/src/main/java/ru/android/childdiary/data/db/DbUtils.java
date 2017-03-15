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
import ru.android.childdiary.data.entities.Models;

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

    private static <T, E> T delete(EntityStore dataStore,
                                   Class<E> entityClass, T object, long objectId) {
        BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
        Object entity = blockingEntityStore.findByKey(entityClass, objectId);
        if (entity != null) {
            blockingEntityStore.delete(entity);
            return object;
        }
        throw new RuntimeException(object.getClass() + " not found while deleting");
    }

    public static <T, E> Observable<T> updateObservable(EntityStore dataStore,
                                                        Class<E> entityClass, T object, long objectId,
                                                        @NonNull BiFunction<E, T, E> updateEntityWithPlainObject,
                                                        @NonNull Function<E, T> mapToPlainObject) {
        return Observable.fromCallable(() -> update(dataStore, entityClass, object, objectId, updateEntityWithPlainObject, mapToPlainObject));
    }

    @SuppressWarnings("unchecked")
    private static <T, E> T update(EntityStore dataStore,
                                   Class<E> entityClass, T object, long objectId,
                                   @NonNull BiFunction<E, T, E> updateEntityWithPlainObject,
                                   @NonNull Function<E, T> mapToPlainObject) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            E entity = (E) blockingEntityStore.findByKey(entityClass, objectId);
            if (entity != null) {
                entity = updateEntityWithPlainObject.apply(entity, object);
                entity = (E) blockingEntityStore.update(entity);
                return mapToPlainObject.apply(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("error while updating " + object, e);
        }
        throw new RuntimeException(object + " not found while updating");
    }

    public static <T, E, RT, RE> Observable<T> addObservable(EntityStore dataStore,
                                                             Class<RE> parentEntityClass, long parentEntityId,
                                                             T object,
                                                             @NonNull BiFunction<T, RE, E> mapToEntity,
                                                             @NonNull Function<E, T> mapToPlainObject) {
        return Observable.fromCallable(() -> add(dataStore, parentEntityClass, parentEntityId, object, mapToEntity, mapToPlainObject));
    }

    @SuppressWarnings("unchecked")
    public static <T, E, RT, RE> T add(EntityStore dataStore,
                                       Class<RE> parentEntityClass, long parentEntityId,
                                       T object,
                                       @NonNull BiFunction<T, RE, E> mapToEntity,
                                       @NonNull Function<E, T> mapToPlainObject) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            RE parentEntity = (RE) blockingEntityStore.findByKey(parentEntityClass, parentEntityId);
            if (parentEntity != null) {
                E entity = mapToEntity.apply(object, parentEntity);
                entity = (E) blockingEntityStore.insert(entity);
                return mapToPlainObject.apply(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("error while inserting " + object, e);
        }
        throw new RuntimeException("parent entity not found while inserting " + object);
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
