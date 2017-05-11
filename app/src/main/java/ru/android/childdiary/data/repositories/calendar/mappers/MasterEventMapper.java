package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.core.RepeatParametersEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class MasterEventMapper implements EntityMapper<MasterEventData, MasterEventEntity, MasterEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public MasterEventMapper(ChildMapper childMapper,
                             RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public MasterEvent mapToPlainObject(@NonNull MasterEventData data) {
        ChildData childData = data.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = data.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return MasterEvent.masterBuilder()
                .masterEventId(data.getId())
                .eventType(data.getEventType())
                .dateTime(data.getDateTime())
                .notifyTimeInMinutes(data.getNotifyTimeInMinutes())
                .note(data.getNote())
                .isDone(data.isDone())
                .repeatParameters(repeatParameters)
                .linearGroup(data.getLinearGroup())
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
        RepeatParameters repeatParameters = masterEvent.getRepeatParameters();
        if (repeatParameters != null) {
            RepeatParametersEntity repeatParametersEntity = (RepeatParametersEntity) blockingEntityStore.findByKey(RepeatParametersEntity.class, repeatParameters.getId());
            masterEventEntity.setRepeatParameters(repeatParametersEntity);
        }
        return masterEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull MasterEventEntity to, @NonNull MasterEvent from) {
        to.setEventType(from.getEventType());
        to.setDateTime(from.getDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setDone(from.getIsDone());
        to.setLinearGroup(from.getLinearGroup());
    }
}
