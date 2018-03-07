package ru.android.healthvector.domain.child;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.child.requests.DeleteChildRequest;
import ru.android.healthvector.domain.child.requests.DeleteChildResponse;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;

public interface ChildRepository {
    Observable<Child> getActiveChild();

    void setActiveChild(@NonNull Child child);

    Observable<Child> getActiveChildOnce();

    Observable<Child> setActiveChildObservable(@NonNull Child child);

    Observable<List<Child>> getAll();

    Observable<Child> add(@NonNull Child child, @NonNull List<ConcreteAchievement> concreteAchievements);

    Observable<Child> update(@NonNull Child child);

    Observable<DeleteChildResponse> delete(@NonNull DeleteChildRequest request);
}
