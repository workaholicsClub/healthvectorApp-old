package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;

class OtherEventMapper {
    public static OtherEvent mapToPlainObject(@NonNull OtherEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        return OtherEvent.builder()
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

    public static OtherEventEntity mapToEntity(@NonNull OtherEvent event) {
        return updateEntityWithPlainObject(new OtherEventEntity(), event);
    }

    public static OtherEventEntity mapToEntity(@NonNull OtherEvent event, @NonNull MasterEventEntity masterEventEntity) {
        OtherEventEntity eventEntity = updateEntityWithPlainObject(new OtherEventEntity(), event);
        eventEntity.setMasterEvent(masterEventEntity);
        return eventEntity;
    }

    public static OtherEventEntity updateEntityWithPlainObject(@NonNull OtherEventEntity to, @NonNull OtherEvent from) {
        to.setFinishDateTime(from.getFinishDateTime());
        return to;
    }
}
