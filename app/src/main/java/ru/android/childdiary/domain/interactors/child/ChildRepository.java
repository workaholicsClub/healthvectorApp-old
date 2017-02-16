package ru.android.childdiary.domain.interactors.child;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;

public interface ChildRepository extends Repository {
    Observable<List<Child>> getAll();

    Observable<Child> add(Child child);

    Observable<Child> update(Child child);

    Observable<Child> delete(Child child);
}
