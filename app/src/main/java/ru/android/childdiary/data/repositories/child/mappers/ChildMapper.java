package ru.android.childdiary.data.repositories.child.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Child;

public class ChildMapper {
    public static Child mapToPlainObject(@NonNull ChildData childData) {
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

    public static ChildEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull Child child) {
        ChildEntity childEntity;
        if (child.getId() == null) {
            childEntity = new ChildEntity();
        } else {
            childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
        }
        fillNonReferencedFields(childEntity, child);
        return childEntity;
    }

    private static void fillNonReferencedFields(@NonNull ChildEntity to, @NonNull Child from) {
        to.setName(from.getName());
        to.setBirthDate(from.getBirthDate());
        to.setBirthTime(from.getBirthTime());
        to.setSex(from.getSex());
        to.setImageFileName(from.getImageFileName());
        to.setBirthHeight(from.getBirthHeight());
        to.setBirthWeight(from.getBirthWeight());
    }
}
