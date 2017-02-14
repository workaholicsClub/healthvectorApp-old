package ru.android.childdiary.data.repositories.child;

import ru.android.childdiary.data.entities.child.AntropometryData;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.models.child.Antropometry;
import ru.android.childdiary.domain.models.child.Child;

class Mapper {
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
        ChildEntity childEntity = new ChildEntity();
        childEntity.setName(child.getName());
        childEntity.setBirthDate(child.getBirthDate());
        childEntity.setBirthTime(child.getBirthTime());
        childEntity.setSex(child.getSex());
        childEntity.setImageFileName(child.getImageFileName());
        childEntity.setHeight(child.getHeight());
        childEntity.setWeight(child.getWeight());
        return childEntity;
    }

    public static Antropometry map(AntropometryData antropometryData) {
        return Antropometry.builder()
                .id(antropometryData.getId())
                .child(Mapper.map(antropometryData.getChild()))
                .height(antropometryData.getHeight())
                .weight(antropometryData.getWeight())
                .date(antropometryData.getDate())
                .build();
    }

    public static AntropometryEntity map(Antropometry antropometry) {
        AntropometryEntity antropometryEntity = new AntropometryEntity();
        antropometryEntity.setChild(Mapper.map(antropometry.getChild()));
        antropometryEntity.setHeight(antropometry.getHeight());
        antropometryEntity.setWeight(antropometry.getWeight());
        antropometryEntity.setDate(antropometry.getDate());
        return antropometryEntity;
    }
}
