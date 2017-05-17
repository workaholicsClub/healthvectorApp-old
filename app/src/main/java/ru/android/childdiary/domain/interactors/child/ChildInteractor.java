package ru.android.childdiary.domain.interactors.child;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationException;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationResult;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidator;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;

public class ChildInteractor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final ChildValidator childValidator;
    private final ImagesRepository imagesRepository;

    @Inject
    public ChildInteractor(ChildDataRepository childRepository,
                           CalendarDataRepository calendarRepository,
                           ChildValidator childValidator,
                           ImagesDataRepository imagesRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.childValidator = childValidator;
        this.imagesRepository = imagesRepository;
    }

    public Observable<Child> getActiveChild() {
        return childRepository.getActiveChild();
    }

    public void setActiveChild(@NonNull Child child) {
        childRepository.setActiveChild(child);
    }

    public Observable<Child> getActiveChildOnce() {
        return childRepository.getActiveChildOnce();
    }

    public Observable<Child> setActiveChildObservable(@NonNull Child child) {
        return childRepository.setActiveChildObservable(child);
    }

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
        return stopSleepTimersBeforeChildDelete(child)
                .flatMap(childRepository::delete)
                .flatMap(this::deleteImageFile);
    }

    private Observable<Child> deleteImageFile(@NonNull Child child) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFile(child.getImageFileName());
            return child;
        });
    }

    private Observable<Child> stopSleepTimersBeforeChildDelete(@NonNull Child child) {
        return calendarRepository.getSleepEvents(GetSleepEventsRequest.builder()
                .child(child)
                .withStartedTimer(true)
                .build())
                .map(GetSleepEventsResponse::getEvents)
                .first(Collections.emptyList())
                .flatMapObservable(Observable::fromIterable)
                .map(event -> event.toBuilder().isTimerStarted(false).build())
                .map(event -> calendarRepository.update(event).blockingFirst())
                .count()
                .toObservable()
                .doOnNext(count -> logger.debug("stopped sleep timers count: " + count))
                .map(count -> child);
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
