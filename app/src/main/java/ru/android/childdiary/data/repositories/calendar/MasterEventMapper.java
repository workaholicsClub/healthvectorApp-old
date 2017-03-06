package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

class MasterEventMapper {
    public static MasterEvent mapToPlainObject(@NonNull MasterEventData masterEventData) {
        return MasterEvent.builder()
                .id(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .build();
    }

    public static MasterEventEntity mapToEntity(@NonNull MasterEvent masterEvent) {
        return updateEntityWithPlainObject(new MasterEventEntity(), masterEvent);
    }

    public static MasterEventEntity mapToEntity(@NonNull MasterEvent masterEvent, @NonNull ChildEntity childEntity) {
        MasterEventEntity masterEventEntity = updateEntityWithPlainObject(new MasterEventEntity(), masterEvent);
        masterEventEntity.setChild(childEntity);
        return masterEventEntity;
    }

    public static MasterEventEntity updateEntityWithPlainObject(@NonNull MasterEventEntity to, @NonNull MasterEvent from) {
        to.setEventType(from.getEventType());
        to.setDescription(from.getDescription());
        to.setDateTime(from.getDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setDone(from.getIsDone());
        to.setDeleted(from.getIsDeleted());
        return to;
    }
}
