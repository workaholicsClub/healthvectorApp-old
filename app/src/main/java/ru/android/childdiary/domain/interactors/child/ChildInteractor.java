package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationException;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationResult;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidator;

public class ChildInteractor implements Interactor, ChildRepository {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final ChildDataRepository childRepository;
    private final ChildValidator childValidator;

    @Inject
    public ChildInteractor(ChildDataRepository childRepository, ChildValidator childValidator) {
        this.childRepository = childRepository;
        this.childValidator = childValidator;
    }

    @Override
    public Observable<Child> getActiveChild() {
        return childRepository.getActiveChild();
    }

    public Observable<Child> getActiveChildOnce() {
        return childRepository.getActiveChild().first(Child.NULL).toObservable();
    }

    @Override
    public Observable<Child> setActiveChild(@NonNull Child child) {
        return childRepository.setActiveChild(child);
    }

    @Override
    public Observable<List<Child>> getAll() {
        return childRepository.getAll();
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
