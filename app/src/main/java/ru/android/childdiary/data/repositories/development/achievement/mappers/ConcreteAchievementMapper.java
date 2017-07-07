package ru.android.childdiary.data.repositories.development.achievement.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.development.achievement.ConcreteAchievementData;
import ru.android.childdiary.data.db.entities.development.achievement.ConcreteAchievementEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;

public class ConcreteAchievementMapper implements EntityMapper<ConcreteAchievementData, ConcreteAchievementEntity, ConcreteAchievement> {
    @Inject
    public ConcreteAchievementMapper() {
    }

    @Override
    public ConcreteAchievement mapToPlainObject(@NonNull ConcreteAchievementData concreteAchievementData) {
        return null;
    }

    @Override
    public ConcreteAchievementEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull ConcreteAchievement concreteAchievement) {
        return null;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ConcreteAchievementEntity to, @NonNull ConcreteAchievement from) {
    }
}
