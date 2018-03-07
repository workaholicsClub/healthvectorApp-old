package ru.android.healthvector.domain.child;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.healthvector.data.repositories.calendar.CalendarDataRepository;
import ru.android.healthvector.data.repositories.child.ChildDataRepository;
import ru.android.healthvector.data.repositories.core.images.ImagesDataRepository;
import ru.android.healthvector.data.repositories.dictionaries.achievements.AchievementDataRepository;
import ru.android.healthvector.domain.calendar.CalendarRepository;
import ru.android.healthvector.domain.calendar.requests.GetSleepEventsRequest;
import ru.android.healthvector.domain.calendar.requests.GetSleepEventsResponse;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.child.requests.DeleteChildRequest;
import ru.android.healthvector.domain.child.requests.DeleteChildResponse;
import ru.android.healthvector.domain.child.validation.ChildValidationResult;
import ru.android.healthvector.domain.child.validation.ChildValidator;
import ru.android.healthvector.domain.core.images.ImageType;
import ru.android.healthvector.domain.core.images.ImagesRepository;
import ru.android.healthvector.domain.core.requests.DeleteResponse;

public class ChildInteractor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final AchievementDataRepository achievementDataRepository;
    private final ChildValidator childValidator;
    private final ImagesRepository imagesRepository;

    @Inject
    public ChildInteractor(ChildDataRepository childRepository,
                           CalendarDataRepository calendarRepository,
                           AchievementDataRepository achievementDataRepository,
                           ChildValidator childValidator,
                           ImagesDataRepository imagesRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.achievementDataRepository = achievementDataRepository;
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
        return childValidator.validateObservable(item)
                .flatMap(this::createImageFile)
                .flatMap(child -> childRepository.add(child,
                        achievementDataRepository.generatePredefinedConcreteAchievements(child)))
                .flatMap(this::setActiveChildObservable);
    }

    public Observable<Child> update(@NonNull Child item) {
        return childValidator.validateObservable(item)
                .flatMap(this::createImageFile)
                .flatMap(childRepository::update);
    }

    public Observable<DeleteChildResponse> delete(@NonNull DeleteChildRequest request) {
        return stopSleepTimersBeforeChildDelete(request.getChild())
                .flatMap(count -> childRepository.delete(request))
                .flatMap(this::deleteImageFiles);
    }

    private <T extends DeleteResponse> Observable<T> deleteImageFiles(@NonNull T response) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFiles(response.getImageFilesToDelete());
            return response;
        });
    }

    private Observable<Long> stopSleepTimersBeforeChildDelete(@NonNull Child child) {
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
                .doOnNext(count -> logger.debug("stopped sleep timers count: " + count));
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
        return childObservable.map(childValidator::validateOnUi);
    }

    private Observable<Child> createImageFile(@NonNull Child child) {
        return Observable.fromCallable(() -> {
            if (imagesRepository.isTemporaryImageFile(child.getImageFileName())) {
                String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(ImageType.PROFILE, child.getImageFileName());
                return child.toBuilder().imageFileName(uniqueImageFileName).build();
            }
            return child;
        });
    }
}
