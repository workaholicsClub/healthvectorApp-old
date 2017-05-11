package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.PumpEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class PumpEventMapper implements EntityMapper<PumpEventData, PumpEventEntity, PumpEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public PumpEventMapper(ChildMapper childMapper,
                           RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public PumpEvent mapToPlainObject(@NonNull PumpEventData data) {
        MasterEventData masterEventData = data.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return PumpEvent.builder()
                .id(data.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .repeatParameters(repeatParameters)
                .linearGroup(masterEventData.getLinearGroup())
                .breast(data.getBreast())
                .leftAmountMl(data.getLeftAmountMl())
                .rightAmountMl(data.getRightAmountMl())
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
