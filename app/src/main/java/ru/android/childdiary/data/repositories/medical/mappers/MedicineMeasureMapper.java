package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureData;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

public class MedicineMeasureMapper implements EntityMapper<MedicineMeasureData, MedicineMeasureEntity, MedicineMeasure> {
    @Inject
    public MedicineMeasureMapper() {
    }

    @Override
    public MedicineMeasure mapToPlainObject(@NonNull MedicineMeasureData medicineMeasureData) {
        return MedicineMeasure.builder()
                .id(medicineMeasureData.getId())
                .name(medicineMeasureData.getName())
                .build();
    }

    @Override
    public MedicineMeasureEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    @Override
    public void fillNonReferencedFields(@NonNull MedicineMeasureEntity to, @NonNull MedicineMeasure from) {
        to.setName(from.getName());
    }
}