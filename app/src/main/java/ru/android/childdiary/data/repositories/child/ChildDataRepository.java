package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class ChildDataRepository implements ChildRepository {
    private static final String KEY_ACTIVE_CHILD_ID = "active_child_id";

    private final RxSharedPreferences preferences;
    private final ChildDbService dbService;

    @Inject
    public ChildDataRepository(RxSharedPreferences preferences, ChildDbService dbService) {
        this.preferences = preferences;
        this.dbService = dbService;
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
        return dbService.getAll();
    }

    @Override
    public Observable<Child> add(@NonNull Child child) {
        return dbService.add(child);
    }

    @Override
    public Observable<Child> update(@NonNull Child child) {
        return dbService.update(child);
    }

    @Override
    public Observable<Child> delete(@NonNull Child child) {
        return dbService.delete(child);
    }
}
