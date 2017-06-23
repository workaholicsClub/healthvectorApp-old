package ru.android.childdiary.data.db;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;
import lombok.val;
import ru.android.childdiary.data.entities.Models;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;

public class DbUtils {
    public static ReactiveEntityStore<Persistable> getDataStore(CustomDatabaseSource source) {
        val configuration = source.getConfiguration();
        val dataStore = ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));
        return dataStore;
    }

    public static CustomDatabaseSource getDatabaseSource(Context appContext, String dbName, int dbVersion) {
        return new CustomDatabaseSource(appContext, Models.DEFAULT, dbName, dbVersion);
    }

    public static <T, E extends Persistable> Observable<T> deleteObservable(BlockingEntityStore<Persistable> blockingEntityStore,
                                                                            Class<E> entityClass, T object, long objectId) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() ->
                delete(blockingEntityStore, entityClass, object, objectId)));
    }

    public static <T, E extends Persistable> T delete(BlockingEntityStore<Persistable> blockingEntityStore,
                                                      Class<E> entityClass, T object, long objectId) {
        E entity = blockingEntityStore.findByKey(entityClass, objectId);
        if (entity != null) {
            blockingEntityStore.delete(entity);
            return object;
        }
        throw new RuntimeException(object.getClass() + " not found while deleting");
    }

    public static <T, E extends D, D extends Persistable> Observable<T> insertObservable(BlockingEntityStore<Persistable> blockingEntityStore,
                                                                                         T object, EntityMapper<D, E, T> mapper) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() ->
                insert(blockingEntityStore, object, mapper)));
    }

    public static <T, E extends D, D extends Persistable> T insert(BlockingEntityStore<Persistable> blockingEntityStore,
                                                                   T object, EntityMapper<D, E, T> mapper) {
        E entity = mapper.mapToEntity(blockingEntityStore, object);
        entity = blockingEntityStore.insert(entity);
        return mapper.mapToPlainObject(entity);
    }

    public static <T, E extends D, D extends Persistable> Observable<T> updateObservable(BlockingEntityStore<Persistable> blockingEntityStore,
                                                                                         T object, EntityMapper<D, E, T> mapper) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() ->
                update(blockingEntityStore, object, mapper)));
    }

    public static <T, E extends D, D extends Persistable> T update(BlockingEntityStore<Persistable> blockingEntityStore,
                                                                   T object, EntityMapper<D, E, T> mapper) {
        E entity = mapper.mapToEntity(blockingEntityStore, object);
        entity = blockingEntityStore.update(entity);
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
