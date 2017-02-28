package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.app.ChildDiaryPreferences;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.utils.ObjectUtils;

public class ChildInteractor implements Interactor, ChildRepository {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final ChildDiaryPreferences preferences;
    private final ChildDataRepository childRepository;
    private final ChildValidator childValidator;

    @Inject
    public ChildInteractor(ChildDiaryPreferences preferences, ChildDataRepository childRepository, ChildValidator childValidator) {
        this.preferences = preferences;
        this.childRepository = childRepository;
        this.childValidator = childValidator;
    }

    @Override
    public Observable<List<Child>> getAll() {
        return childRepository.getAll();
    }

    @Override
    public Observable<Child> get(@NonNull Long id) {
        return childRepository.get(id);
    }

    @Override
    public Observable<Child> add(@NonNull Child item) {
        return validate(item)
                .flatMap(childRepository::add)
                .flatMap(this::setActiveChild);
    }

    @Override
    public Observable<Child> update(@NonNull Child item) {
        return childRepository.update(item);
    }

    @Override
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

    public Observable<Child> getActiveChild(@NonNull List<Child> childList) {
        if (childList.isEmpty()) {
            return Observable.just(Child.NULL);
        } else {
            return Observable
                    .fromIterable(childList)
                    .filter(child -> ObjectUtils.equals(child.getId(), preferences.getActiveChildId()))
                    .firstElement()
                    .defaultIfEmpty(childList.get(0))
                    .toObservable();
        }
    }

    public Observable<Child> setActiveChild(@NonNull List<Child> childList, @Nullable Long id) {
        if (childList.isEmpty()) {
            return Observable.just(Child.NULL);
        } else {
            return Observable
                    .fromIterable(childList)
                    .filter(child -> ObjectUtils.equals(child.getId(), id))
                    .firstElement()
                    .defaultIfEmpty(childList.get(0))
                    .toObservable()
                    .flatMap(this::setActiveChild);
        }
    }

    private Observable<Child> setActiveChild(@NonNull Child child) {
        return Observable.fromCallable(() -> {
            preferences.setActiveChildId(child.getId());
            return child;
        });
    }

    public Observable<Boolean> controlDoneButton(@NonNull Observable<Child> childObservable) {
        return childObservable
                .map(child -> !TextUtils.isEmpty(child.getName()))
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
