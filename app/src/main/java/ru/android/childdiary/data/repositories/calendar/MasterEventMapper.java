package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class MasterEventMapper {
    public static MasterEvent mapToPlainObject(@NonNull MasterEventData masterEventData) {
        return MasterEvent.masterBuilder()
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .build();
    }

    public static MasterEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                @NonNull MasterEvent masterEvent) {
        return mapToEntity(blockingEntityStore, masterEvent, null);
    }

    public static MasterEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                @NonNull MasterEvent masterEvent,
                                                @Nullable Child child) {
        MasterEventEntity masterEventEntity;
        if (masterEvent.getMasterEventId() == null) {
            masterEventEntity = new MasterEventEntity();
        } else {
            masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
        }
        fillNonReferencedFields(masterEventEntity, masterEvent);
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            masterEventEntity.setChild(childEntity);
        }
        return masterEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull MasterEventEntity to, @NonNull MasterEvent from) {
        to.setEventType(from.getEventType());
        to.setDescription(from.getDescription());
        to.setDateTime(from.getDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setDone(from.getIsDone());
        to.setDeleted(from.getIsDeleted());
    }
}
