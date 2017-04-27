package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.child.Child;

class PumpEventMapper {
    public static PumpEvent mapToPlainObject(@NonNull PumpEventEntity eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        return PumpEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .child(child)
                .breast(eventData.getBreast())
                .leftAmountMl(eventData.getLeftAmountMl())
                .rightAmountMl(eventData.getRightAmountMl())
                .build();
    }

    public static PumpEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                              @NonNull PumpEvent pumpEvent) {
        return mapToEntity(blockingEntityStore, pumpEvent, null);
    }

    public static PumpEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                              @NonNull PumpEvent pumpEvent,
                                              @Nullable MasterEvent masterEvent) {
        PumpEventEntity pumpEventEntity;
        if (pumpEvent.getId() == null) {
            pumpEventEntity = new PumpEventEntity();
        } else {
            pumpEventEntity = (PumpEventEntity) blockingEntityStore.findByKey(PumpEventEntity.class, pumpEvent.getId());
        }
        fillNonReferencedFields(pumpEventEntity, pumpEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            pumpEventEntity.setMasterEvent(masterEventEntity);
        }
        return pumpEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull PumpEventEntity to, @NonNull PumpEvent from) {
        to.setBreast(from.getBreast());
        to.setLeftAmountMl(from.getLeftAmountMl());
        to.setRightAmountMl(from.getRightAmountMl());
    }
}
