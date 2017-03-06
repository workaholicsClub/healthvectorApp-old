package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;

public class DiaperEventMapper {
    public static DiaperEvent mapToPlainObject(@NonNull DiaperEventData diaperEventData) {
        return DiaperEvent.builder()
                .id(diaperEventData.getId())
                .masterEvent(MasterEventMapper.mapToPlainObject(diaperEventData.getMasterEvent()))
                .diaperState(diaperEventData.getDiaperState())
                .build();
    }

    public static DiaperEventEntity mapToEntity(@NonNull DiaperEvent diaperEvent) {
        return updateEntityWithPlainObject(new DiaperEventEntity(), diaperEvent);
    }

    public static DiaperEventEntity mapToEntity(@NonNull DiaperEvent diaperEvent, @NonNull MasterEventEntity masterEventEntity) {
        DiaperEventEntity diaperEventEntity = updateEntityWithPlainObject(new DiaperEventEntity(), diaperEvent);
        diaperEventEntity.setMasterEvent(masterEventEntity);
        return diaperEventEntity;
    }

    public static DiaperEventEntity updateEntityWithPlainObject(@NonNull DiaperEventEntity to, @NonNull DiaperEvent from) {
        to.setDiaperState(from.getDiaperState());
        return to;
    }
}
