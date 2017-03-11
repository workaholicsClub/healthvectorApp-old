package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.app.ChildDiaryPreferences;
import ru.android.childdiary.data.repositories.core.events.ActiveChildChangedEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class ChildDataRepository implements ChildRepository {
    private final EventBus bus;
    private final ChildDiaryPreferences preferences;
    private final ChildDbService dbService;

    @Inject
    public ChildDataRepository(EventBus bus, ChildDiaryPreferences preferences, ChildDbService dbService) {
        this.bus = bus;
        this.preferences = preferences;
        this.dbService = dbService;
    }

    public Observable<Child> getActiveChild() {
        return getAll().flatMap(this::getActiveChild);
    }

    public Observable<Child> getActiveChild(@NonNull List<Child> childList) {
        return Observable
                .fromIterable(childList)
                .filter(child -> ObjectUtils.equals(child.getId(), preferences.getActiveChildId()))
                .first(Child.NULL)
                .toObservable()
                .switchIfEmpty(Observable.just(Child.NULL));
    }

    public Observable<Child> setActiveChild(@NonNull Child child) {
        return Observable.fromCallable(() -> {
            preferences.setActiveChildId(child.getId());
            bus.post(ActiveChildChangedEvent.builder().child(child).build());
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
