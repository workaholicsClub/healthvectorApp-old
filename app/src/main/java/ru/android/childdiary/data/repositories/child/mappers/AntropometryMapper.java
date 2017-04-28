package ru.android.childdiary.data.repositories.child.mappers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.child.AntropometryData;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;

public class AntropometryMapper {
    public static Antropometry mapToPlainObject(@NonNull AntropometryData antropometryData) {
        return Antropometry.builder()
                .id(antropometryData.getId())
                .height(antropometryData.getHeight())
                .weight(antropometryData.getWeight())
                .date(antropometryData.getDate())
                .build();
    }

    public static AntropometryEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                 @NonNull Antropometry antropometry) {
        return mapToEntity(blockingEntityStore, antropometry, null);
    }

    public static AntropometryEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                 @NonNull Antropometry antropometry,
                                                 @Nullable Child child) {
        AntropometryEntity antropometryEntity;
        if (antropometry.getId() == null) {
            antropometryEntity = new AntropometryEntity();
        } else {
            antropometryEntity = (AntropometryEntity) blockingEntityStore.findByKey(AntropometryEntity.class, antropometry.getId());
        }
        fillNonReferencedFields(antropometryEntity, antropometry);
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            antropometryEntity.setChild(childEntity);
        }
        return antropometryEntity;
    }

    private static void fillNonReferencedFields(@NonNull AntropometryEntity to, @NonNull Antropometry from) {
        to.setHeight(from.getHeight());
        to.setWeight(from.getWeight());
        to.setDate(from.getDate());
    }
}
