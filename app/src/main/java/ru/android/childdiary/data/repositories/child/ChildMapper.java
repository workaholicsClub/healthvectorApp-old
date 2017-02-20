package ru.android.childdiary.data.repositories.child;

import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Child;

class ChildMapper {
    public static Child map(ChildData childData) {
        return Child.builder()
                .id(childData.getId())
                .name(childData.getName())
                .birthDate(childData.getBirthDate())
                .birthTime(childData.getBirthTime())
                .sex(childData.getSex())
                .imageFileName(childData.getImageFileName())
                .height(childData.getHeight())
                .weight(childData.getWeight())
                .build();
    }

    public static ChildEntity map(Child child) {
        return copy(new ChildEntity(), child);
    }

    public static ChildEntity copy(ChildEntity toChildEntity, Child fromChild) {
        toChildEntity.setName(fromChild.getName());
        toChildEntity.setBirthDate(fromChild.getBirthDate());
        toChildEntity.setBirthTime(fromChild.getBirthTime());
        toChildEntity.setSex(fromChild.getSex());
        toChildEntity.setImageFileName(fromChild.getImageFileName());
        toChildEntity.setHeight(fromChild.getHeight());
        toChildEntity.setWeight(fromChild.getWeight());
        return toChildEntity;
    }
}
