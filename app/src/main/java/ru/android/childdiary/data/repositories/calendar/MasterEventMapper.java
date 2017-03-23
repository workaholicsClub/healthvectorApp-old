package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.repositories.child.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class MasterEventMapper {
    public static MasterEvent mapToPlainObject(@NonNull MasterEventData masterEventData) {
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        return MasterEvent.masterBuilder()
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .child(child)
                .build();
    }

    public static MasterEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                @NonNull MasterEvent masterEvent) {
        MasterEventEntity masterEventEntity;
        if (masterEvent.getMasterEventId() == null) {
            masterEventEntity = new MasterEventEntity();
        } else {
            masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
        }
        fillNonReferencedFields(masterEventEntity, masterEvent);
        Child child = masterEvent.getChild();
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
