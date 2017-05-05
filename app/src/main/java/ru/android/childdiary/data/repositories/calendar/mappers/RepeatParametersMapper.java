package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersEntity;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;

public class RepeatParametersMapper {
    public static RepeatParameters mapToPlainObject(@NonNull RepeatParametersData repeatParametersData) {
        return RepeatParameters.builder()
                .id(repeatParametersData.getId())
                .linearGroups(repeatParametersData.getLinearGroups())
                .periodicityInMinutes(repeatParametersData.getPeriodicityInMinutes())
                .lengthInMinutes(repeatParametersData.getLengthInMinutes())
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
        to.setLinearGroups(from.getLinearGroups());
        to.setPeriodicityInMinutes(from.getPeriodicityInMinutes());
        to.setLengthInMinutes(from.getLengthInMinutes());
    }
}
