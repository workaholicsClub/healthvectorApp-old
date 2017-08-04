package ru.android.childdiary.data.repositories.dictionaries.achievements.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementData;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;

public class AchievementMapper implements EntityMapper<AchievementData, AchievementEntity, Achievement> {
    @Inject
    public AchievementMapper() {
    }

    @Override
    public Achievement mapToPlainObject(@NonNull AchievementData achievementData) {
        return Achievement.builder()
                .id(achievementData.getId())
                .name(achievementData.getName())
                .isPredefined(achievementData.isPredefined())
                .orderNumber(achievementData.getOrderNumber())
                .build();
    }

    @Override
    public AchievementEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull Achievement achievement) {
        AchievementEntity achievementEntity;
        if (achievement.getId() == null) {
            achievementEntity = new AchievementEntity();
        } else {
            achievementEntity = (AchievementEntity) blockingEntityStore.findByKey(AchievementEntity.class, achievement.getId());
        }
        fillNonReferencedFields(achievementEntity, achievement);
        return achievementEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull AchievementEntity to, @NonNull Achievement from) {
        to.setName(from.getName());
        to.setPredefined(from.getIsPredefined());
        to.setOrderNumber(from.getOrderNumber());
    }
}