package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public interface ChildRepository {
    Observable<Child> getActiveChild();

    void setActiveChild(@NonNull Child child);

    Observable<Child> getActiveChildOnce();

    Observable<Child> setActiveChildObservable(@NonNull Child child);

    Observable<List<Child>> getAll();

    Observable<Child> add(@NonNull Child child);

    Observable<Child> update(@NonNull Child child);

    Observable<Child> delete(@NonNull Child child);
}
