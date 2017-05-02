package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventData;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

public class MedicineTakingEventMapper {
    public static MedicineTakingEvent mapToPlainObject(@NonNull MedicineTakingEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        MedicineTakingData medicineTakingData = eventData.getMedicineTaking();
        MedicineTaking medicineTaking = medicineTakingData == null ? null : MedicineTakingMapper.mapToPlainObject(medicineTakingData);
        return MedicineTakingEvent.builder()
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
                .medicineTaking(medicineTaking)
                .build();
    }

    public static MedicineTakingEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                        @NonNull MedicineTakingEvent medicineTakingEvent) {
        return mapToEntity(blockingEntityStore, medicineTakingEvent, null);
    }

    public static MedicineTakingEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                        @NonNull MedicineTakingEvent medicineTakingEvent,
                                                        @Nullable MasterEvent masterEvent) {
        MedicineTakingEventEntity medicineTakingEventEntity;
        if (medicineTakingEvent.getId() == null) {
            medicineTakingEventEntity = new MedicineTakingEventEntity();
        } else {
            medicineTakingEventEntity = (MedicineTakingEventEntity) blockingEntityStore.findByKey(MedicineTakingEvent.class, medicineTakingEvent.getId());
        }
        fillNonReferencedFields(medicineTakingEventEntity, medicineTakingEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            medicineTakingEventEntity.setMasterEvent(masterEventEntity);
        }
        MedicineTaking medicineTaking = medicineTakingEvent.getMedicineTaking();
        if (medicineTaking != null) {
            MedicineTakingEntity medicineTakingEntity = (MedicineTakingEntity) blockingEntityStore.findByKey(MedicineTakingEntity.class, medicineTaking.getId());
            medicineTakingEventEntity.setMedicineTaking(medicineTakingEntity);
        }
        return medicineTakingEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull MedicineTakingEventEntity to, @NonNull MedicineTakingEvent from) {
    }
}
