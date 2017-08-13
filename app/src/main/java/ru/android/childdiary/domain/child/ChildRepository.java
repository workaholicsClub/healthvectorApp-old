package ru.android.childdiary.domain.child;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.child.requests.DeleteChildRequest;
import ru.android.childdiary.domain.child.requests.DeleteChildResponse;

public interface ChildRepository {
    Observable<Child> getActiveChild();

    void setActiveChild(@NonNull Child child);

    Observable<Child> getActiveChildOnce();

    Observable<Child> setActiveChildObservable(@NonNull Child child);

    Observable<List<Child>> getAll();

    Observable<Child> add(@NonNull Child child);

    Observable<Child> update(@NonNull Child child);

    Observable<DeleteChildResponse> delete(@NonNull DeleteChildRequest request);
}
