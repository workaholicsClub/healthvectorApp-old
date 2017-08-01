package ru.android.childdiary.domain.interactors.development.achievement;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.data.repositories.development.achievement.AchievementDataRepository;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.development.achievement.validation.AchievementValidator;
import ru.android.childdiary.domain.interactors.development.achievement.validation.ConcreteAchievementValidator;

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

    public Observable<List<Achievement>> getAchievements() {
        return achievementRepository.getAchievements();
    }

    public Observable<Achievement> add(@NonNull Achievement achievement) {
        return achievementValidator.validateObservable(achievement)
                .flatMap(achievementRepository::add);
    }

    public Observable<Achievement> delete(@NonNull Achievement achievement) {
        return achievementRepository.delete(achievement);
    }
}
