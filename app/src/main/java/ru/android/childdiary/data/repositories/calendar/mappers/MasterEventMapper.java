package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.core.RepeatParametersEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class MasterEventMapper {
    public static MasterEvent mapToPlainObject(@NonNull MasterEventData masterEventData) {
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        return MasterEvent.masterBuilder()
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .repeatParameters(repeatParameters)
                .linearGroup(masterEventData.getLinearGroup())
                .child(child)
                .build();
    }

    public static MasterEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    private static void fillNonReferencedFields(@NonNull MasterEventEntity to, @NonNull MasterEvent from) {
        to.setEventType(from.getEventType());
        to.setDateTime(from.getDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setDone(from.getIsDone());
        to.setLinearGroup(from.getLinearGroup());
    }
}
