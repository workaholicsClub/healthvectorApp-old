package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.core.RepeatParametersEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class RepeatParametersMapper implements EntityMapper<RepeatParametersData, RepeatParametersEntity, RepeatParameters> {
    @Inject
    public RepeatParametersMapper() {
    }

    @Override
    public RepeatParameters mapToPlainObject(@NonNull RepeatParametersData data) {
        return RepeatParameters.builder()
                .id(data.getId())
                .frequency(data.getFrequency())
                .periodicity(data.getPeriodicity())
                .length(data.getLength())
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
