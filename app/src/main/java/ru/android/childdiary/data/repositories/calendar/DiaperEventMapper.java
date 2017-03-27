package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class DiaperEventMapper {
    public static DiaperEvent mapToPlainObject(@NonNull DiaperEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        return DiaperEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .child(child)
                .diaperState(eventData.getDiaperState())
                .build();
    }

    public static DiaperEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                @NonNull DiaperEvent diaperEvent) {
        return mapToEntity(blockingEntityStore, diaperEvent, null);
    }

    public static DiaperEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                @NonNull DiaperEvent diaperEvent,
                                                @Nullable MasterEvent masterEvent) {
        DiaperEventEntity diaperEventEntity;
        if (diaperEvent.getId() == null) {
            diaperEventEntity = new DiaperEventEntity();
        } else {
            diaperEventEntity = (DiaperEventEntity) blockingEntityStore.findByKey(DiaperEventEntity.class, diaperEvent.getId());
        }
        fillNonReferencedFields(diaperEventEntity, diaperEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            diaperEventEntity.setMasterEvent(masterEventEntity);
        }
        return diaperEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull DiaperEventEntity to, @NonNull DiaperEvent from) {
        to.setDiaperState(from.getDiaperState());
    }
}
