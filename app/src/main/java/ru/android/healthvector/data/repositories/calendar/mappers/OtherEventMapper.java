package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.OtherEventData;
import ru.android.healthvector.data.db.entities.calendar.standard.OtherEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.domain.child.data.Child;

public class OtherEventMapper implements EntityMapper<OtherEventData, OtherEventEntity, OtherEvent> {
    private final ChildMapper childMapper;

    @Inject
    public OtherEventMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public OtherEvent mapToPlainObject(@NonNull OtherEventData otherEventData) {
        MasterEventData masterEventData = otherEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return OtherEvent.builder()
                .id(otherEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyDateTime(masterEventData.getNotifyDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .name(otherEventData.getName())
                .finishDateTime(otherEventData.getFinishDateTime())
                .build();
    }

    @Override
    public OtherEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                        @NonNull OtherEvent otherEvent) {
        OtherEventEntity otherEventEntity;
        if (otherEvent.getId() == null) {
            otherEventEntity = new OtherEventEntity();
        } else {
            otherEventEntity = (OtherEventEntity) blockingEntityStore.findByKey(OtherEventEntity.class, otherEvent.getId());
        }
        fillNonReferencedFields(otherEventEntity, otherEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, otherEvent.getMasterEventId());
        otherEventEntity.setMasterEvent(masterEventEntity);

        return otherEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull OtherEventEntity to, @NonNull OtherEvent from) {
        to.setName(from.getName());
        to.setFinishDateTime(from.getFinishDateTime());
    }
}
