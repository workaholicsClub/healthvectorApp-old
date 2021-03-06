package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.child.ChildEntity;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.child.data.Child;

public class MasterEventMapper implements EntityMapper<MasterEventData, MasterEventEntity, MasterEvent> {
    private final ChildMapper childMapper;

    @Inject
    public MasterEventMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public MasterEvent mapToPlainObject(@NonNull MasterEventData masterEventData) {
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return MasterEvent.masterBuilder()
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyDateTime(masterEventData.getNotifyDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .linearGroup(masterEventData.getLinearGroup())
                .child(child)
                .build();
    }

    @Override
    public MasterEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    @Override
    public void fillNonReferencedFields(@NonNull MasterEventEntity to, @NonNull MasterEvent from) {
        to.setEventType(from.getEventType());
        to.setDateTime(from.getDateTime());
        to.setNotifyDateTime(from.getNotifyDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setDone(from.getIsDone());
        to.setLinearGroup(from.getLinearGroup());
    }
}
