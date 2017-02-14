package ru.android.childdiary.domain.core;

import java.util.List;

import io.reactivex.Single;

public interface CrudRepository<T> extends Repository {
    Single<List<T>> getAll();

    Single<T> add(T item);

    Single<T> update(T item);

    Single<T> delete(T item);
}
