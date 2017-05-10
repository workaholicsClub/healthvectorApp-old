package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.core.RepeatParametersEntity;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class RepeatParametersMapper {
    public static RepeatParameters mapToPlainObject(@NonNull RepeatParametersData repeatParametersData) {
        return RepeatParameters.builder()
                .id(repeatParametersData.getId())
                .frequency(repeatParametersData.getFrequency())
                .periodicity(repeatParametersData.getPeriodicity())
                .length(repeatParametersData.getLength())
                .build();
    }

    public static RepeatParametersEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull RepeatParameters repeatParameters) {
        RepeatParametersEntity repeatParametersEntity;
        if (repeatParameters.getId() == null) {
            repeatParametersEntity = new RepeatParametersEntity();
        } else {
            repeatParametersEntity = (RepeatParametersEntity) blockingEntityStore.findByKey(RepeatParametersEntity.class, repeatParameters.getId());
        }
        fillNonReferencedFields(repeatParametersEntity, repeatParameters);
        return repeatParametersEntity;
    }

    private static void fillNonReferencedFields(@NonNull RepeatParametersEntity to, @NonNull RepeatParameters from) {
        to.setFrequency(from.getFrequency());
        to.setPeriodicity(from.getPeriodicity());
        to.setLength(from.getLength());
    }
}
