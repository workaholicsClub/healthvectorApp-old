package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class SleepEventMapper {
    public static SleepEvent mapToPlainObject(@NonNull SleepEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        return SleepEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .child(child)
                .finishDateTime(eventData.getFinishDateTime())
                .isTimerStarted(eventData.isTimerStarted())
                .build();
    }

    public static SleepEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                               @NonNull SleepEvent sleepEvent) {
        return mapToEntity(blockingEntityStore, sleepEvent, null);
    }

    public static SleepEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                               @NonNull SleepEvent sleepEvent,
                                               @Nullable MasterEvent masterEvent) {
        SleepEventEntity sleepEventEntity;
        if (sleepEvent.getId() == null) {
            sleepEventEntity = new SleepEventEntity();
        } else {
            sleepEventEntity = (SleepEventEntity) blockingEntityStore.findByKey(SleepEventEntity.class, sleepEvent.getId());
        }
        fillNonReferencedFields(sleepEventEntity, sleepEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            sleepEventEntity.setMasterEvent(masterEventEntity);
        }
        return sleepEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull SleepEventEntity to, @NonNull SleepEvent from) {
        to.setFinishDateTime(from.getFinishDateTime());
        to.setTimerStarted(from.getIsTimerStarted());
    }
}
