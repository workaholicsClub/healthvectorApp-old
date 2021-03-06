package ru.android.healthvector.domain.development.achievement;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.healthvector.data.repositories.core.images.ImagesDataRepository;
import ru.android.healthvector.data.repositories.development.achievement.ConcreteAchievementDataRepository;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.images.ImageType;
import ru.android.healthvector.domain.core.images.ImagesRepository;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.healthvector.domain.development.achievement.validation.AchievementValidationResult;
import ru.android.healthvector.domain.development.achievement.validation.ConcreteAchievementValidator;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;

public class ConcreteAchievementInteractor {
    private final ConcreteAchievementRepository concreteAchievementRepository;
    private final ConcreteAchievementValidator concreteAchievementValidator;
    private final ImagesRepository imagesRepository;

    @Inject
    public ConcreteAchievementInteractor(ConcreteAchievementDataRepository achievementRepository,
                                         ConcreteAchievementValidator concreteAchievementValidator,
                                         ImagesDataRepository imagesRepository) {
        this.concreteAchievementRepository = achievementRepository;
        this.concreteAchievementValidator = concreteAchievementValidator;
        this.imagesRepository = imagesRepository;
    }

    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return concreteAchievementRepository.getConcreteAchievements(child);
    }

    public Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(concreteAchievementRepository::add);
    }

    public Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(concreteAchievementRepository::update)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request) {
        return concreteAchievementRepository.delete(request)
                .flatMap(this::deleteImageFiles);
    }

    private Observable<UpsertConcreteAchievementRequest> validate(@NonNull UpsertConcreteAchievementRequest request) {
        return concreteAchievementValidator.validateObservable(request.getConcreteAchievement())
                .map(concreteAchievement -> request.toBuilder().concreteAchievement(concreteAchievement).build());
    }

    private Observable<UpsertConcreteAchievementRequest> createImageFile(@NonNull UpsertConcreteAchievementRequest request) {
        return Observable.fromCallable(() -> {
            ConcreteAchievement concreteAchievement = request.getConcreteAchievement();
            if (imagesRepository.isTemporaryImageFile(concreteAchievement.getImageFileName())) {
                String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(ImageType.ACHIEVEMENT, concreteAchievement.getImageFileName());
                concreteAchievement = concreteAchievement.toBuilder().imageFileName(uniqueImageFileName).build();
                return request.toBuilder().concreteAchievement(concreteAchievement).build();
            }
            return request;
        });
    }

    private <T extends DeleteResponse> Observable<T> deleteImageFiles(@NonNull T response) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFiles(response.getImageFilesToDelete());
            return response;
        });
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable,
            @NonNull FieldValueChangeEventsObservable<AchievementType> achievementTypeObservable) {
        return Observable.combineLatest(
                nameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString)
                        .map(String::trim),
                achievementTypeObservable,
                (name, achievementTypeEvent) -> ConcreteAchievement.builder()
                        .nameUser(name)
                        .achievementType(achievementTypeEvent.getValue())
                        .build())
                .map(concreteAchievementValidator::validateOnUi)
                .map(concreteAchievementValidator::isValid)
                .distinctUntilChanged();
    }

    public Observable<List<AchievementValidationResult>> controlFields(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable,
            @NonNull FieldValueChangeEventsObservable<AchievementType> achievementTypeObservable) {
        return Observable.combineLatest(
                nameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString)
                        .map(String::trim),
                achievementTypeObservable,
                (name, achievementTypeEvent) -> ConcreteAchievement.builder()
                        .nameUser(name)
                        .achievementType(achievementTypeEvent.getValue())
                        .build())
                .map(concreteAchievementValidator::validateOnUi);
    }

    public Observable<ConcreteAchievement> getDefaultConcreteAchievement(@NonNull Child child) {
        return Observable.just(ConcreteAchievement.builder()
                .child(child)
                .date(getDefaultDate(child))
                .build());
    }

    private LocalDate getDefaultDate(@NonNull Child child) {
        LocalDate today = LocalDate.now();
        if (child.getBirthDate() == null) {
            return today;
        }
        if (today.isBefore(child.getBirthDate())) {
            return child.getBirthDate();
        }
        return today;
    }
}
