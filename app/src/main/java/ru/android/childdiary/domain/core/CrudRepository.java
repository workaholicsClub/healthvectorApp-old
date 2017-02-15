package ru.android.childdiary.domain.core;

import java.util.List;

import io.reactivex.Observable;

public interface CrudRepository<T> extends Repository {
    Observable<List<T>> getAll();

    Observable<T> add(T item);

    Observable<T> update(T item);

    Observable<T> delete(T item);
}
