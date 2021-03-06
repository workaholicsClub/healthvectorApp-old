package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.DiaperEventData;
import ru.android.healthvector.data.db.entities.calendar.standard.DiaperEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.calendar.data.standard.DiaperEvent;
import ru.android.healthvector.domain.child.data.Child;

public class DiaperEventMapper implements EntityMapper<DiaperEventData, DiaperEventEntity, DiaperEvent> {
    private final ChildMapper childMapper;

    @Inject
    public DiaperEventMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public DiaperEvent mapToPlainObject(@NonNull DiaperEventData diaperEventData) {
        MasterEventData masterEventData = diaperEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return DiaperEvent.builder()
                .id(diaperEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyDateTime(masterEventData.getNotifyDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .diaperState(diaperEventData.getDiaperState())
                .build();
    }

    @Override
    public DiaperEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                         @NonNull DiaperEvent diaperEvent) {
        DiaperEventEntity diaperEventEntity;
        if (diaperEvent.getId() == null) {
            diaperEventEntity = new DiaperEventEntity();
        } else {
            diaperEventEntity = (DiaperEventEntity) blockingEntityStore.findByKey(DiaperEventEntity.class, diaperEvent.getId());
        }
        fillNonReferencedFields(diaperEventEntity, diaperEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, diaperEvent.getMasterEventId());
        diaperEventEntity.setMasterEvent(masterEventEntity);

        return diaperEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull DiaperEventEntity to, @NonNull DiaperEvent from) {
        to.setDiaperState(from.getDiaperState());
    }
}
