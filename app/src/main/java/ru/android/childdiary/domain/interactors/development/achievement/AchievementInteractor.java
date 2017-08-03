package ru.android.childdiary.domain.interactors.development.achievement;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.data.repositories.development.achievement.AchievementDataRepository;
import ru.android.childdiary.domain.interactors.core.requests.DeleteResponse;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.dictionaries.GetAchievementsRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.development.achievement.validation.AchievementValidationResult;
import ru.android.childdiary.domain.interactors.development.achievement.validation.AchievementValidator;
import ru.android.childdiary.domain.interactors.development.achievement.validation.ConcreteAchievementValidator;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.Achievement;

public class AchievementInteractor {
    private final AchievementRepository achievementRepository;
    private final ConcreteAchievementValidator concreteAchievementValidator;
    private final AchievementValidator achievementValidator;
    private final ImagesRepository imagesRepository;

    @Inject
    public AchievementInteractor(AchievementDataRepository achievementRepository,
                                 ConcreteAchievementValidator concreteAchievementValidator,
                                 AchievementValidator achievementValidator,
                                 ImagesDataRepository imagesRepository) {
        this.achievementRepository = achievementRepository;
        this.concreteAchievementValidator = concreteAchievementValidator;
        this.achievementValidator = achievementValidator;
        this.imagesRepository = imagesRepository;
    }

    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return achievementRepository.getConcreteAchievements(child);
    }

    public Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(achievementRepository::add);
    }

    public Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(achievementRepository::update)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request) {
        return achievementRepository.delete(request)
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

    public Observable<List<Achievement>> getAchievements(@NonNull GetAchievementsRequest request) {
        return achievementRepository.getAchievements(request);
    }

    public Observable<Achievement> add(@NonNull Achievement achievement) {
        return achievementValidator.validateObservable(achievement)
                .flatMap(achievementRepository::add);
    }

    public Observable<Achievement> delete(@NonNull Achievement achievement) {
        return achievementRepository.delete(achievement);
    }

    public Observable<Boolean> controlDoneButtonConcreteAchievement(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(name -> ConcreteAchievement.builder()
                        .name(name)
                        .build())
                .map(concreteAchievementValidator::validate)
                .map(concreteAchievementValidator::isValid)
                .distinctUntilChanged();
    }

    public Observable<List<AchievementValidationResult>> controlFieldsConcreteAchievement(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(name -> ConcreteAchievement.builder()
                        .name(name)
                        .build())
                .map(concreteAchievementValidator::validate);
    }

    public Observable<Boolean> controlDoneButtonAchievement(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(name -> !TextUtils.isEmpty(name))
                .distinctUntilChanged();
    }

    public Observable<List<AchievementValidationResult>> controlFieldsAchievement(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(name -> Achievement.builder().name(name).build())
                .map(achievementValidator::validate);
    }

    public Observable<ConcreteAchievement> getDefaultConcreteAchievement(@NonNull Child child) {
        return Observable.just(ConcreteAchievement.builder()
                .child(child)
                .date(getDefaultDate(child))
                .build());
    }

    private LocalDate getDefaultDate(@NonNull Child child) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(child.getBirthDate())) {
            return child.getBirthDate();
        }
        return today;
    }
}
