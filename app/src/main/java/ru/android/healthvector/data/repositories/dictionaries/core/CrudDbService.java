package ru.android.healthvector.data.repositories.dictionaries.core;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public interface CrudDbService<T> {
    Observable<List<T>> getAll();

    Observable<T> add(@NonNull T item);

    Observable<T> update(@NonNull T item);

    Observable<T> delete(@NonNull T item);
}
