package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface ValueRepository<T> {
    Observable<T> getSelectedValue();

    void setSelectedValue(@NonNull T value);

    Observable<T> getSelectedValueOnce();

    Observable<T> setSelectedValueObservable(@NonNull T value);
}
