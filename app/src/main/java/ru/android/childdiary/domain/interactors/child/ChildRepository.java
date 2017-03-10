package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;

public interface ChildRepository extends Repository {
    Observable<List<Child>> getAll();

    Observable<Child> add(@NonNull Child child);

    Observable<Child> update(@NonNull Child child);

    Observable<Child> delete(@NonNull Child child);
}
