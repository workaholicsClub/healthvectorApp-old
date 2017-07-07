package ru.android.childdiary.domain.interactors.development.achievement;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.development.achievement.AchievementDataRepository;

public class AchievementInteractor {
    private final AchievementRepository achievementRepository;

    @Inject
    public AchievementInteractor(AchievementDataRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }
}
