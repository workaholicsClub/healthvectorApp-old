package ru.android.childdiary.data.repositories.development.achievement.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.development.ConcreteAchievementData;
import ru.android.childdiary.data.db.entities.development.ConcreteAchievementEntity;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementData;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.dictionaries.achievements.mappers.AchievementMapper;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.dictionaries.achievements.data.Achievement;

public class ConcreteAchievementMapper implements EntityMapper<ConcreteAchievementData, ConcreteAchievementEntity, ConcreteAchievement> {
    private final ChildMapper childMapper;
    private final AchievementMapper achievementMapper;

    @Inject
    public ConcreteAchievementMapper(ChildMapper childMapper,
                                     AchievementMapper achievementMapper) {
        this.childMapper = childMapper;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public ConcreteAchievement mapToPlainObject(@NonNull ConcreteAchievementData concreteAchievementData) {
        ChildData childData = concreteAchievementData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        AchievementData achievementData = concreteAchievementData.getAchievement();
        Achievement achievement = achievementData == null ? null : achievementMapper.mapToPlainObject(achievementData);
        return ConcreteAchievement.builder()
                .id(concreteAchievementData.getId())
                .child(child)
                .achievement(achievement)
                .name(concreteAchievementData.getName())
                .date(concreteAchievementData.getConcreteAchievementDate())
                .note(concreteAchievementData.getNote())
                .imageFileName(concreteAchievementData.getImageFileName())
                .isPredefined(concreteAchievementData.isPredefined())
                .build();
    }

    @Override
    public ConcreteAchievementEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull ConcreteAchievement concreteAchievement) {
        ConcreteAchievementEntity concreteAchievementEntity;
        if (concreteAchievement.getId() == null) {
            concreteAchievementEntity = new ConcreteAchievementEntity();
        } else {
            concreteAchievementEntity = (ConcreteAchievementEntity) blockingEntityStore.findByKey(ConcreteAchievementEntity.class, concreteAchievement.getId());
        }
        fillNonReferencedFields(concreteAchievementEntity, concreteAchievement);
        Child child = concreteAchievement.getChild();
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            concreteAchievementEntity.setChild(childEntity);
        }
        Achievement achievement = concreteAchievement.getAchievement();
        if (achievement != null) {
            AchievementEntity achievementEntity = (AchievementEntity) blockingEntityStore.findByKey(AchievementEntity.class, achievement.getId());
            concreteAchievementEntity.setAchievement(achievementEntity);
        }
        return concreteAchievementEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ConcreteAchievementEntity to, @NonNull ConcreteAchievement from) {
        to.setName(from.getName());
        to.setConcreteAchievementDate(from.getDate());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
        to.setPredefined(from.getIsPredefined());
    }
}
