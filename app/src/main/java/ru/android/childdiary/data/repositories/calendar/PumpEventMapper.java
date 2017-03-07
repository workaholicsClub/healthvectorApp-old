package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;

class PumpEventMapper {
    public static PumpEvent mapToPlainObject(@NonNull PumpEventEntity eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        return PumpEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .breast(eventData.getBreast())
                .durationInMinutes(eventData.getDurationInMinutes())
                .build();
    }

    public static PumpEventEntity mapToEntity(@NonNull PumpEvent event) {
        return updateEntityWithPlainObject(new PumpEventEntity(), event);
    }

    public static PumpEventEntity mapToEntity(@NonNull PumpEvent event, @NonNull MasterEventEntity masterEventEntity) {
        PumpEventEntity eventEntity = updateEntityWithPlainObject(new PumpEventEntity(), event);
        eventEntity.setMasterEvent(masterEventEntity);
        return eventEntity;
    }

    public static PumpEventEntity updateEntityWithPlainObject(@NonNull PumpEventEntity to, @NonNull PumpEvent from) {
        to.setBreast(from.getBreast());
        to.setDurationInMinutes(from.getDurationInMinutes());
        return to;
    }
}
