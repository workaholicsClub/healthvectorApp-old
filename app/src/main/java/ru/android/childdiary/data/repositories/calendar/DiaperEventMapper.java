package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;

class DiaperEventMapper {
    public static DiaperEvent mapToPlainObject(@NonNull DiaperEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        return DiaperEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .diaperState(eventData.getDiaperState())
                .build();
    }

    public static DiaperEventEntity mapToEntity(@NonNull DiaperEvent event) {
        return updateEntityWithPlainObject(new DiaperEventEntity(), event);
    }

    public static DiaperEventEntity mapToEntity(@NonNull DiaperEvent event, @NonNull MasterEventEntity masterEventEntity) {
        DiaperEventEntity eventEntity = updateEntityWithPlainObject(new DiaperEventEntity(), event);
        eventEntity.setMasterEvent(masterEventEntity);
        return eventEntity;
    }

    public static DiaperEventEntity updateEntityWithPlainObject(@NonNull DiaperEventEntity to, @NonNull DiaperEvent from) {
        to.setDiaperState(from.getDiaperState());
        return to;
    }
}
