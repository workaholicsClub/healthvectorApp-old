package ru.android.healthvector.data.repositories.dictionaries.medicinemeasure.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineMeasureData;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineMeasureEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;

public class MedicineMeasureMapper implements EntityMapper<MedicineMeasureData, MedicineMeasureEntity, MedicineMeasure> {
    @Inject
    public MedicineMeasureMapper() {
    }

    @Override
    public MedicineMeasure mapToPlainObject(@NonNull MedicineMeasureData medicineMeasureData) {
        return MedicineMeasure.builder()
                .id(medicineMeasureData.getId())
                .nameEn(medicineMeasureData.getNameEn())
                .nameRu(medicineMeasureData.getNameRu())
                .nameUser(medicineMeasureData.getNameUser())
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
        to.setNameEn(from.getNameEn());
        to.setNameRu(from.getNameRu());
        to.setNameUser(from.getNameUser());
    }
}
