package ru.android.childdiary.data.db;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.reactivex.Observable;
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
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;

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

    public static <T, E> Observable<T> deleteObservable(EntityStore dataStore,
                                                        Class<E> entityClass, T object, long objectId) {
        //noinspection unchecked
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

    public static <T, E extends D, D> Observable<T> insertObservable(EntityStore dataStore,
                                                                     T object, EntityMapper<D, E, T> mapper) {
        //noinspection unchecked
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                insert(dataStore, object, mapper)));
    }

    public static <T, E extends D, D> T insert(EntityStore dataStore,
                                               T object, EntityMapper<D, E, T> mapper) {
        BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
        E entity = mapper.mapToEntity(blockingEntityStore, object);
        //noinspection unchecked
        entity = (E) blockingEntityStore.insert(entity);
        return mapper.mapToPlainObject(entity);
    }

    public static <T, E extends D, D> Observable<T> updateObservable(EntityStore dataStore,
                                                                     T object, EntityMapper<D, E, T> mapper) {
        //noinspection unchecked
        return Observable.fromCallable(() -> (T) dataStore.toBlocking().runInTransaction(() ->
                update(dataStore, object, mapper)));
    }

    public static <T, E extends D, D> T update(EntityStore dataStore,
                                               T object, EntityMapper<D, E, T> mapper) {
        BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
        E entity = mapper.mapToEntity(blockingEntityStore, object);
        //noinspection unchecked
        entity = (E) blockingEntityStore.update(entity);
        return mapper.mapToPlainObject(entity);
    }

    public static <T, E extends D, D> Observable<List<T>> mapReactiveResultToListObservable(ReactiveResult<E> reactiveResult,
                                                                                            EntityMapper<D, E, T> mapper) {
        return reactiveResult
                .observable()
                .map(mapper::mapToPlainObject)
                .toList()
                .toObservable();
    }

    public static <T, E extends D, D> Observable<T> mapReactiveResultToObservable(ReactiveResult<E> reactiveResult,
                                                                                  EntityMapper<D, E, T> mapper) {
        return reactiveResult
                .observable()
                .map(mapper::mapToPlainObject)
                .firstOrError()
                .toObservable();
    }
}
