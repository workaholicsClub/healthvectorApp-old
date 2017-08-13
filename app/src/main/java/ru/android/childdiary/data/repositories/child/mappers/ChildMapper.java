package ru.android.childdiary.data.repositories.child.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.child.data.Child;

public class ChildMapper implements EntityMapper<ChildData, ChildEntity, Child> {
    @Inject
    public ChildMapper() {
    }

    @Override
    public Child mapToPlainObject(@NonNull ChildData childData) {
        return Child.builder()
                .id(childData.getId())
                .name(childData.getName())
                .birthDate(childData.getBirthDate())
                .birthTime(childData.getBirthTime())
                .sex(childData.getSex())
                .imageFileName(childData.getImageFileName())
                .birthHeight(childData.getBirthHeight())
                .birthWeight(childData.getBirthWeight())
                .build();
    }

    @Override
    public ChildEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull Child child) {
        ChildEntity childEntity;
        if (child.getId() == null) {
            childEntity = new ChildEntity();
        } else {
            childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
        }
        fillNonReferencedFields(childEntity, child);
        return childEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ChildEntity to, @NonNull Child from) {
        to.setName(from.getName());
        to.setBirthDate(from.getBirthDate());
        to.setBirthTime(from.getBirthTime());
        to.setSex(from.getSex());
        to.setImageFileName(from.getImageFileName());
        to.setBirthHeight(from.getBirthHeight());
        to.setBirthWeight(from.getBirthWeight());
    }
}
