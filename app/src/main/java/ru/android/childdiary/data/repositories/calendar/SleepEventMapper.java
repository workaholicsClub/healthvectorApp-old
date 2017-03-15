package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;

class SleepEventMapper {
    public static SleepEvent mapToPlainObject(@NonNull SleepEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        return SleepEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .finishDateTime(eventData.getFinishDateTime())
                .build();
    }

    public static SleepEventEntity mapToEntity(@NonNull SleepEvent event) {
        return updateEntityWithPlainObject(new SleepEventEntity(), event);
    }

    public static SleepEventEntity mapToEntity(@NonNull SleepEvent event, @NonNull MasterEventEntity masterEventEntity) {
        SleepEventEntity eventEntity = updateEntityWithPlainObject(new SleepEventEntity(), event);
        eventEntity.setMasterEvent(masterEventEntity);
        return eventEntity;
    }

    public static SleepEventEntity updateEntityWithPlainObject(@NonNull SleepEventEntity to, @NonNull SleepEvent from) {
        to.setFinishDateTime(from.getFinishDateTime());
        return to;
    }
}
