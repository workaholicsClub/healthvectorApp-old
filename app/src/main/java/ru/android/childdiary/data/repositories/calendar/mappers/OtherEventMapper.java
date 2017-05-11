package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.OtherEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class OtherEventMapper {
    public static OtherEvent mapToPlainObject(@NonNull OtherEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        return OtherEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .repeatParameters(repeatParameters)
                .linearGroup(masterEventData.getLinearGroup())
                .name(eventData.getName())
                .finishDateTime(eventData.getFinishDateTime())
                .build();
    }

    public static OtherEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    private static void fillNonReferencedFields(@NonNull OtherEventEntity to, @NonNull OtherEvent from) {
        to.setName(from.getName());
        to.setFinishDateTime(from.getFinishDateTime());
    }
}
