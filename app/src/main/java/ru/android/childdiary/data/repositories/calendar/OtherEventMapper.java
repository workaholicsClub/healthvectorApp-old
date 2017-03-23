package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class OtherEventMapper {
    public static OtherEvent mapToPlainObject(@NonNull OtherEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
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
                .child(child)
                .title(eventData.getTitle())
                .finishDateTime(eventData.getFinishDateTime())
                .build();
    }

    public static OtherEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                               @NonNull OtherEvent otherEvent) {
        return mapToEntity(blockingEntityStore, otherEvent, null);
    }

    public static OtherEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                               @NonNull OtherEvent otherEvent,
                                               @Nullable MasterEvent masterEvent) {
        OtherEventEntity otherEventEntity;
        if (otherEvent.getId() == null) {
            otherEventEntity = new OtherEventEntity();
        } else {
            otherEventEntity = (OtherEventEntity) blockingEntityStore.findByKey(OtherEventEntity.class, otherEvent.getId());
        }
        fillNonReferencedFields(otherEventEntity, otherEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            otherEventEntity.setMasterEvent(masterEventEntity);
        }
        return otherEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull OtherEventEntity to, @NonNull OtherEvent from) {
        to.setTitle(from.getTitle());
        to.setFinishDateTime(from.getFinishDateTime());
    }
}
