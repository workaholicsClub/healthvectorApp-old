package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.app.ChildDiaryPreferences;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.core.events.ActiveChildChangedEvent;
import ru.android.childdiary.utils.ObjectUtils;

public class ChildInteractor implements Interactor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final EventBus bus;
    private final ChildDiaryPreferences preferences;
    private final ChildDataRepository childRepository;
    private final ChildValidator childValidator;

    @Inject
    public ChildInteractor(EventBus bus, ChildDiaryPreferences preferences, ChildDataRepository childRepository, ChildValidator childValidator) {
        this.bus = bus;
        this.preferences = preferences;
        this.childRepository = childRepository;
        this.childValidator = childValidator;
    }

    public Observable<ChildResponse> getAll() {
        return childRepository.getAll()
                .map(childList -> ChildResponse.builder().childList(childList).build())
                .flatMap(response ->
                        getActiveChild(response.getChildList())
                                .map(child -> response.toBuilder().activeChild(child).build())
                );
    }

    public Observable<Child> get(@NonNull Long id) {
        return childRepository.get(id);
    }

    public Observable<Child> add(@NonNull Child item) {
        return validate(item)
                .flatMap(childRepository::add)
                .flatMap(this::setActiveChild);
    }

    public Observable<Child> update(@NonNull Child item) {
        return validate(item)
                .flatMap(childRepository::update);
    }

    public Observable<Child> delete(@NonNull Child item) {
        return childRepository.delete(item)
                .doOnNext(child -> {
                    String path = child.getImageFileName();
                    if (path != null) {
                        boolean result = new File(path).delete();
                        if (result) {
                            logger.debug("file " + path + " deleted");
                        } else {
                            logger.error("file " + path + " not deleted");
                        }
                    }
                });
    }

    public Observable<Child> getActiveChild() {
        return childRepository.getAll().flatMap(this::getActiveChild);
    }

    private Observable<Child> getActiveChild(@NonNull List<Child> childList) {
        if (childList.isEmpty()) {
            return Observable.just(Child.NULL);
        } else {
            return Observable
                    .fromIterable(childList)
                    .filter(child -> ObjectUtils.equals(child.getId(), preferences.getActiveChildId()))
                    .first(childList.get(0))
                    .toObservable();
        }
    }

    public Observable<Child> setActiveChild(@Nullable Long id) {
        return childRepository.getAll()
                .first(Collections.emptyList())
                .flatMapObservable(Observable::fromIterable)
                .filter(child -> ObjectUtils.equals(child.getId(), id))
                .first(Child.NULL)
                .toObservable()
                .flatMap(this::setActiveChild);
    }

    private Observable<Child> setActiveChild(@NonNull Child child) {
        return Observable.fromCallable(() -> {
            preferences.setActiveChildId(child.getId());
            bus.post(ActiveChildChangedEvent.builder().child(child).build());
            return child;
        });
    }

    public Observable<Boolean> controlDoneButton(@NonNull Observable<Child> childObservable) {
        return childObservable
                .map(child -> !TextUtils.isEmpty(child.getName())
                        && child.getSex() != null
                        && child.getBirthDate() != null
                        && child.getBirthHeight() != null
                        && child.getBirthWeight() != null)
                .distinctUntilChanged();
    }

    public Observable<List<ChildValidationResult>> controlFields(@NonNull Observable<Child> childObservable) {
        return childObservable.map(childValidator::validate);
    }

    private Observable<Child> validate(@NonNull Child item) {
        return Observable.just(item)
                .flatMap(child -> {
                    List<ChildValidationResult> results = childValidator.validate(child);
                    if (!childValidator.isValid(results)) {
                        return Observable.error(new ChildValidationException(results));
                    }
                    return Observable.just(child);
                });
    }
}
