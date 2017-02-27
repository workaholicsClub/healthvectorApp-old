package ru.android.childdiary.data.repositories.child;

import ru.android.childdiary.data.entities.child.AntropometryData;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.domain.interactors.child.Antropometry;

// TODO: foreign key mapper
class AntropometryMapper {
    public static Antropometry map(AntropometryData antropometryData) {
        return Antropometry.builder()
                .id(antropometryData.getId())
                .child(ChildMapper.map(antropometryData.getChild()))
                .height(antropometryData.getHeight())
                .weight(antropometryData.getWeight())
                .date(antropometryData.getDate())
                .build();
    }

    public static AntropometryEntity map(Antropometry antropometry) {
        return copy(new AntropometryEntity(), antropometry);
    }

    public static AntropometryEntity copy(AntropometryEntity toAntropometryEntity, Antropometry fromAntropometry) {
        toAntropometryEntity.setHeight(fromAntropometry.getHeight());
        toAntropometryEntity.setWeight(fromAntropometry.getWeight());
        toAntropometryEntity.setDate(fromAntropometry.getDate());
        return toAntropometryEntity;
    }
}
