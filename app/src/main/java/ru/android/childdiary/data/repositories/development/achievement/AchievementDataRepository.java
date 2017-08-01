package ru.android.childdiary.data.repositories.development.achievement;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.achievement.Achievement;
import ru.android.childdiary.domain.interactors.development.achievement.AchievementRepository;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;

@Singleton
public class AchievementDataRepository implements AchievementRepository {
    private final AchievementDbService achievementDbService;

    @Inject
    public AchievementDataRepository(AchievementDbService achievementDbService) {
        this.achievementDbService = achievementDbService;
    }

    @Override
    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return achievementDbService.getConcreteAchievements(child);
    }

    @Override
    public Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request) {
        return achievementDbService.add(request);
    }

    @Override
    public Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request) {
        return achievementDbService.update(request);
    }

    @Override
    public Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request) {
        return achievementDbService.delete(request);
    }

    @Override
    public Observable<List<Achievement>> getAchievements() {
        return achievementDbService.getAchievements();
    }

    @Override
    public Observable<Achievement> add(@NonNull Achievement achievement) {
        return achievementDbService.add(achievement);
    }

    @Override
    public Observable<Achievement> delete(@NonNull Achievement achievement) {
        return achievementDbService.delete(achievement);
    }
}
