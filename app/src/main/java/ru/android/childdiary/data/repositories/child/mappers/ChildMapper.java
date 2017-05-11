package ru.android.childdiary.data.repositories.child.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.child.Child;

public class ChildMapper implements EntityMapper<ChildData, ChildEntity, Child> {
    @Inject
    public ChildMapper() {
    }

    @Override
    public Child mapToPlainObject(@NonNull ChildData data) {
        return Child.builder()
                .id(data.getId())
                .name(data.getName())
                .birthDate(data.getBirthDate())
                .birthTime(data.getBirthTime())
                .sex(data.getSex())
                .imageFileName(data.getImageFileName())
                .birthHeight(data.getBirthHeight())
                .birthWeight(data.getBirthWeight())
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
