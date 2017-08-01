package ru.android.childdiary.data.repositories.development.achievement.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.development.achievement.ConcreteAchievementData;
import ru.android.childdiary.data.db.entities.development.achievement.ConcreteAchievementEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;

public class ConcreteAchievementMapper implements EntityMapper<ConcreteAchievementData, ConcreteAchievementEntity, ConcreteAchievement> {
    private final ChildMapper childMapper;

    @Inject
    public ConcreteAchievementMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public ConcreteAchievement mapToPlainObject(@NonNull ConcreteAchievementData concreteAchievementData) {
        ChildData childData = concreteAchievementData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        return ConcreteAchievement.builder()
                .id(concreteAchievementData.getId())
                .child(child)
                .name(concreteAchievementData.getName())
                .date(concreteAchievementData.getConcreteAchievementDate())
                .note(concreteAchievementData.getNote())
                .imageFileName(concreteAchievementData.getImageFileName())
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
        to.setName(from.getName());
        to.setConcreteAchievementDate(from.getDate());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
    }
}
