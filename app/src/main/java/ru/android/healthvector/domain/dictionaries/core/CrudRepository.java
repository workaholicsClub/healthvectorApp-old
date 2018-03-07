package ru.android.healthvector.domain.dictionaries.core;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public interface CrudRepository<T> {
    Observable<List<T>> getAll();

    Observable<T> add(@NonNull T item);

    Observable<T> update(@NonNull T item);

    Observable<T> delete(@NonNull T item);
}
