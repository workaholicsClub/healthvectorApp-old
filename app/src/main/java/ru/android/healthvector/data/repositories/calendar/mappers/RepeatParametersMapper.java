package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.core.RepeatParametersData;
import ru.android.healthvector.data.db.entities.calendar.core.RepeatParametersEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;

public class RepeatParametersMapper implements EntityMapper<RepeatParametersData, RepeatParametersEntity, RepeatParameters> {
    @Inject
    public RepeatParametersMapper() {
    }

    @Override
    public RepeatParameters mapToPlainObject(@NonNull RepeatParametersData repeatParametersData) {
        return RepeatParameters.builder()
                .id(repeatParametersData.getId())
                .frequency(repeatParametersData.getFrequency())
                .periodicity(repeatParametersData.getPeriodicity())
                .length(repeatParametersData.getLength())
                .build();
    }

    @Override
    public RepeatParametersEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull RepeatParameters repeatParameters) {
        RepeatParametersEntity repeatParametersEntity;
        if (repeatParameters.getId() == null) {
            repeatParametersEntity = new RepeatParametersEntity();
        } else {
            repeatParametersEntity = (RepeatParametersEntity) blockingEntityStore.findByKey(RepeatParametersEntity.class, repeatParameters.getId());
        }
        fillNonReferencedFields(repeatParametersEntity, repeatParameters);
        return repeatParametersEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull RepeatParametersEntity to, @NonNull RepeatParameters from) {
        to.setFrequency(from.getFrequency());
        to.setPeriodicity(from.getPeriodicity());
        to.setLength(from.getLength());
    }
}
