package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventData;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.childdiary.data.db.entities.calendar.standard.PumpEventData;
import ru.android.childdiary.data.db.entities.calendar.standard.PumpEventEntity;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.child.data.Child;

public class PumpEventMapper implements EntityMapper<PumpEventData, PumpEventEntity, PumpEvent> {
    private final ChildMapper childMapper;

    @Inject
    public PumpEventMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public PumpEvent mapToPlainObject(@NonNull PumpEventData pumpEventData) {
        MasterEventData masterEventData = pumpEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return PumpEvent.builder()
                .id(pumpEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .breast(pumpEventData.getBreast())
                .leftAmountMl(pumpEventData.getLeftAmountMl())
                .rightAmountMl(pumpEventData.getRightAmountMl())
                .build();
    }

    @Override
    public PumpEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                       @NonNull PumpEvent pumpEvent) {
        PumpEventEntity pumpEventEntity;
        if (pumpEvent.getId() == null) {
            pumpEventEntity = new PumpEventEntity();
        } else {
            pumpEventEntity = (PumpEventEntity) blockingEntityStore.findByKey(PumpEventEntity.class, pumpEvent.getId());
        }
        fillNonReferencedFields(pumpEventEntity, pumpEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, pumpEvent.getMasterEventId());
        pumpEventEntity.setMasterEvent(masterEventEntity);

        return pumpEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull PumpEventEntity to, @NonNull PumpEvent from) {
        to.setBreast(from.getBreast());
        to.setLeftAmountMl(from.getLeftAmountMl());
        to.setRightAmountMl(from.getRightAmountMl());
    }
}
