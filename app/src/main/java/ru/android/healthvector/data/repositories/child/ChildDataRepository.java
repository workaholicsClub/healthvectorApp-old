package ru.android.healthvector.data.repositories.child;

import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.healthvector.data.repositories.calendar.CleanUpDbService;
import ru.android.healthvector.domain.child.ChildRepository;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.child.requests.DeleteChildRequest;
import ru.android.healthvector.domain.child.requests.DeleteChildResponse;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.utils.ObjectUtils;

@Singleton
public class ChildDataRepository implements ChildRepository {
    private static final String KEY_ACTIVE_CHILD_ID = "active_child_id";

    private final RxSharedPreferences preferences;
    private final ChildDbService childDbService;
    private final CleanUpDbService cleanUpDbService;

    @Inject
    public ChildDataRepository(RxSharedPreferences preferences,
                               ChildDbService childDbService,
                               CleanUpDbService cleanUpDbService) {
        this.preferences = preferences;
        this.childDbService = childDbService;
        this.cleanUpDbService = cleanUpDbService;
    }

    @Override
    public Observable<Child> getActiveChild() {
        return Observable.combineLatest(
                getAll(),
                preferences.getLong(KEY_ACTIVE_CHILD_ID).asObservable(),
                this::getActiveChild);
    }

    @Override
    public void setActiveChild(@NonNull Child child) {
        preferences.getLong(KEY_ACTIVE_CHILD_ID).set(child.getId());
    }

    private Child getActiveChild(@NonNull List<Child> childList, Long id) {
        return childList.isEmpty() ? Child.NULL : Observable
                .fromIterable(childList)
                .filter(child -> ObjectUtils.equals(child.getId(), id))
                .first(childList.get(0))
                .blockingGet();
    }

    @Override
    public Observable<Child> getActiveChildOnce() {
        return getActiveChild().first(Child.NULL).toObservable();
    }

    @Override
    public Observable<Child> setActiveChildObservable(@NonNull Child child) {
        return Observable.fromCallable(() -> {
            setActiveChild(child);
            return child;
        });
    }

    @Override
    public Observable<List<Child>> getAll() {
        return childDbService.getAll();
    }

    @Override
    public Observable<Child> add(@NonNull Child child, @NonNull List<ConcreteAchievement> concreteAchievements) {
        return childDbService.add(child, concreteAchievements);
    }

    @Override
    public Observable<Child> update(@NonNull Child child) {
        return childDbService.update(child);
    }

    @Override
    public Observable<DeleteChildResponse> delete(@NonNull DeleteChildRequest request) {
        return cleanUpDbService.deleteChild(request);
    }
}
