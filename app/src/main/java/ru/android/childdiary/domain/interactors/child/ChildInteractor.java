package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationException;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationResult;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidator;
import ru.android.childdiary.utils.EventHelper;

public class ChildInteractor implements Interactor, ChildRepository {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final ChildValidator childValidator;

    @Inject
    public ChildInteractor(ChildDataRepository childRepository,
                           CalendarDataRepository calendarRepository,
                           ChildValidator childValidator) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.childValidator = childValidator;
    }

    @Override
    public Observable<Child> getActiveChild() {
        return childRepository.getActiveChild();
    }

    @Override
    public void setActiveChild(@NonNull Child child) {
        childRepository.setActiveChild(child);
    }

    @Override
    public Observable<Child> getActiveChildOnce() {
        return childRepository.getActiveChildOnce();
    }

    @Override
    public Observable<Child> setActiveChildObservable(@NonNull Child child) {
        return childRepository.setActiveChildObservable(child);
    }

    @Override
    public Observable<List<Child>> getAll() {
        return childRepository.getAll();
    }

    public Observable<Child> add(@NonNull Child item) {
        return validate(item)
                .flatMap(childRepository::add)
                .flatMap(this::setActiveChildObservable);
    }

    public Observable<Child> update(@NonNull Child item) {
        return validate(item)
                .flatMap(childRepository::update);
    }

    public Observable<Child> delete(@NonNull Child child) {
        return stopSleepTimers(child)
                .flatMap(this::deleteInternal);
    }

    private Observable<Child> stopSleepTimers(@NonNull Child child) {
        return calendarRepository.getSleepEventsWithTimer()
                .first(Collections.emptyList())
                .flatMapObservable(Observable::fromIterable)
                .filter(event -> EventHelper.sameChild(child, event))
                .map(calendarRepository::stopTimer)
                .count()
                .toObservable()
                .doOnNext(count -> logger.debug("stopped sleep timers count: " + count))
                .map(count -> child);
    }

    private Observable<Child> deleteInternal(@NonNull Child child) {
        return childRepository.delete(child)
                .doOnNext(deletedChild -> {
                    String path = deletedChild.getImageFileName();
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
