package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureData;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

public class MedicineMeasureMapper {
    public static MedicineMeasure mapToPlainObject(@NonNull MedicineMeasureData medicineMeasureData) {
        return MedicineMeasure.builder()
                .id(medicineMeasureData.getId())
                .name(medicineMeasureData.getName())
                .build();
    }

    public static MedicineMeasureEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                    @NonNull MedicineMeasure medicineMeasure) {
        MedicineMeasureEntity medicineMeasureEntity;
        if (medicineMeasure.getId() == null) {
            medicineMeasureEntity = new MedicineMeasureEntity();
        } else {
            medicineMeasureEntity = (MedicineMeasureEntity) blockingEntityStore.findByKey(MedicineMeasureEntity.class, medicineMeasure.getId());
        }
        fillNonReferencedFields(medicineMeasureEntity, medicineMeasure);
        return medicineMeasureEntity;
    }

    private static void fillNonReferencedFields(@NonNull MedicineMeasureEntity to, @NonNull MedicineMeasure from) {
        to.setName(from.getName());
    }
}
