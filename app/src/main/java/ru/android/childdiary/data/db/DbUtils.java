package ru.android.childdiary.data.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
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

    @SuppressWarnings("unchecked")
    public static <T, E> Observable<T> deleteObservable(EntityStore dataStore, Class<E> entityClass, T object, long objectId) {
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                delete(dataStore, entityClass, object, objectId)));
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

    @SuppressWarnings("unchecked")
    public static <T, E> Observable<T> insertObservable(EntityStore dataStore, T object,
                                                        @NonNull BiFunction<BlockingEntityStore, T, E> mapToEntity,
                                                        @NonNull Function<E, T> mapToPlainObject) {
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                insert(dataStore, object, mapToEntity, mapToPlainObject)));
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T insert(EntityStore dataStore, T object,
                                  @NonNull BiFunction<BlockingEntityStore, T, E> mapToEntity,
                                  @NonNull Function<E, T> mapToPlainObject) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            E entity = mapToEntity.apply(blockingEntityStore, object);
            entity = (E) blockingEntityStore.insert(entity);
            return mapToPlainObject.apply(entity);
        } catch (Exception e) {
            throw new RuntimeException("error on inserting\n" + object, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E, PT> Observable<T> insertObservable(EntityStore dataStore, T object, PT parentObject,
                                                            @NonNull Function3<BlockingEntityStore, T, PT, E> mapToEntity,
                                                            @NonNull Function<E, T> mapToPlainObject) {
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                insert(dataStore, object, parentObject, mapToEntity, mapToPlainObject)));
    }

    @SuppressWarnings("unchecked")
    public static <T, E, PT> T insert(EntityStore dataStore, T object, PT parentObject,
                                      @NonNull Function3<BlockingEntityStore, T, PT, E> mapToEntity,
                                      @NonNull Function<E, T> mapToPlainObject) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            E entity = mapToEntity.apply(blockingEntityStore, object, parentObject);
            entity = (E) blockingEntityStore.insert(entity);
            return mapToPlainObject.apply(entity);
        } catch (Exception e) {
            throw new RuntimeException("error on inserting\n" + object + "\n" + parentObject, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E> Observable<T> updateObservable(EntityStore dataStore, T object,
                                                        @NonNull BiFunction<BlockingEntityStore, T, E> mapToEntity,
                                                        @NonNull Function<E, T> mapToPlainObject) {
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                update(dataStore, object, mapToEntity, mapToPlainObject)));
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T update(EntityStore dataStore, T object,
                                  @NonNull BiFunction<BlockingEntityStore, T, E> mapToEntity,
                                  @NonNull Function<E, T> mapToPlainObject) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            E entity = mapToEntity.apply(blockingEntityStore, object);
            entity = (E) blockingEntityStore.update(entity);
            return mapToPlainObject.apply(entity);
        } catch (Exception e) {
            throw new RuntimeException("error on updating\n" + object, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E, PT> Observable<T> updateObservable(EntityStore dataStore, T object, PT parentObject,
                                                            @NonNull Function3<BlockingEntityStore, T, PT, E> mapToEntity,
                                                            @NonNull Function<E, T> mapToPlainObject) {
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                update(dataStore, object, parentObject, mapToEntity, mapToPlainObject)));
    }

    @SuppressWarnings("unchecked")
    public static <T, E, PT> T update(EntityStore dataStore, T object, PT parentObject,
                                      @NonNull Function3<BlockingEntityStore, T, PT, E> mapToEntity,
                                      @NonNull Function<E, T> mapToPlainObject) {
        try {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            E entity = mapToEntity.apply(blockingEntityStore, object, parentObject);
            entity = (E) blockingEntityStore.update(entity);
            return mapToPlainObject.apply(entity);
        } catch (Exception e) {
            throw new RuntimeException("error on updating\n" + object + "\n" + parentObject, e);
        }
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
