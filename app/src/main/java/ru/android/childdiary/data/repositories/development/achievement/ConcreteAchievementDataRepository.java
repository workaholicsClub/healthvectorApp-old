package ru.android.childdiary.data.repositories.development.achievement;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievementRepository;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;

@Singleton
public class ConcreteAchievementDataRepository implements ConcreteAchievementRepository {
    private final ConcreteAchievementDbService concreteAchievementDbService;

    @Inject
    public ConcreteAchievementDataRepository(ConcreteAchievementDbService concreteAchievementDbService) {
        this.concreteAchievementDbService = concreteAchievementDbService;
    }

    @Override
    public Observable<List<ConcreteAchievement>> getConcreteAchievements(@NonNull Child child) {
        return concreteAchievementDbService.getConcreteAchievements(child);
    }

    @Override
    public Observable<UpsertConcreteAchievementResponse> add(@NonNull UpsertConcreteAchievementRequest request) {
        return concreteAchievementDbService.add(request);
    }

    @Override
    public Observable<UpsertConcreteAchievementResponse> update(@NonNull UpsertConcreteAchievementRequest request) {
        return concreteAchievementDbService.update(request);
    }

    @Override
    public Observable<DeleteConcreteAchievementResponse> delete(@NonNull DeleteConcreteAchievementRequest request) {
        return concreteAchievementDbService.delete(request);
    }
}
