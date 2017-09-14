package ru.android.childdiary.data.repositories.development.achievement.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.development.ConcreteAchievementData;
import ru.android.childdiary.data.db.entities.development.ConcreteAchievementEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.dictionaries.achievements.mappers.AchievementMapper;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;

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
        return ConcreteAchievement.builder()
                .id(concreteAchievementData.getId())
                .child(child)
                .achievementType(concreteAchievementData.getAchievementType())
                .nameEn(concreteAchievementData.getNameEn())
                .nameRu(concreteAchievementData.getNameRu())
                .nameUser(concreteAchievementData.getNameUser())
                .date(concreteAchievementData.getConcreteAchievementDate())
                .note(concreteAchievementData.getNote())
                .imageFileName(concreteAchievementData.getImageFileName())
                .isPredefined(concreteAchievementData.isPredefined())
                .fromAge(concreteAchievementData.getFromAge())
                .toAge(concreteAchievementData.getToAge())
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
        return concreteAchievementEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ConcreteAchievementEntity to, @NonNull ConcreteAchievement from) {
        to.setAchievementType(from.getAchievementType());
        to.setNameEn(from.getNameEn());
        to.setNameRu(from.getNameRu());
        to.setNameUser(from.getNameUser());
        to.setConcreteAchievementDate(from.getDate());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
        to.setPredefined(from.getIsPredefined());
        to.setFromAge(from.getFromAge());
        to.setToAge(from.getToAge());
    }
}
