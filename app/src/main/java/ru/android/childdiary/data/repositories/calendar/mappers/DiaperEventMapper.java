package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class DiaperEventMapper implements EntityMapper<DiaperEventData, DiaperEventEntity, DiaperEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public DiaperEventMapper(ChildMapper childMapper,
                             RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public DiaperEvent mapToPlainObject(@NonNull DiaperEventData data) {
        MasterEventData masterEventData = data.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return DiaperEvent.builder()
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
                .diaperState(data.getDiaperState())
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
