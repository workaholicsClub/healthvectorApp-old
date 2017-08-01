package ru.android.childdiary.data.repositories.development.achievement;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.domain.interactors.development.achievement.AchievementRepository;

@Singleton
public class AchievementDataRepository implements AchievementRepository {
    private final AchievementDbService achievementDbService;

    @Inject
    public AchievementDataRepository(AchievementDbService achievementDbService) {
        this.achievementDbService = achievementDbService;
    }
}
