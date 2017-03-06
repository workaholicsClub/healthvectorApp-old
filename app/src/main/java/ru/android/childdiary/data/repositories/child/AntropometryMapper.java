package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.child.AntropometryData;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Antropometry;

class AntropometryMapper {
    public static Antropometry mapToPlainObject(@NonNull AntropometryData antropometryData) {
        return Antropometry.builder()
                .id(antropometryData.getId())
                .height(antropometryData.getHeight())
                .weight(antropometryData.getWeight())
                .date(antropometryData.getDate())
                .build();
    }

    public static AntropometryEntity mapToEntity(@NonNull Antropometry antropometry) {
        return updateEntityWithPlainObject(new AntropometryEntity(), antropometry);
    }

    public static AntropometryEntity mapToEntity(@NonNull Antropometry antropometry, @NonNull ChildEntity childEntity) {
        AntropometryEntity antropometryEntity = updateEntityWithPlainObject(new AntropometryEntity(), antropometry);
        antropometryEntity.setChild(childEntity);
        return antropometryEntity;
    }

    public static AntropometryEntity updateEntityWithPlainObject(@NonNull AntropometryEntity to, @NonNull Antropometry from) {
        to.setHeight(from.getHeight());
        to.setWeight(from.getWeight());
        to.setDate(from.getDate());
        return to;
    }
}
