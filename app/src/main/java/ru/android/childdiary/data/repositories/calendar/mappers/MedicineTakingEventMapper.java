package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventData;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

public class MedicineTakingEventMapper implements EntityMapper<MedicineTakingEventData, MedicineTakingEventEntity, MedicineTakingEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;
    private final MedicineTakingMapper medicineTakingMapper;

    @Inject
    public MedicineTakingEventMapper(ChildMapper childMapper,
                                     RepeatParametersMapper repeatParametersMapper,
                                     MedicineTakingMapper medicineTakingMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
        this.medicineTakingMapper = medicineTakingMapper;
    }

    @Override
    public MedicineTakingEvent mapToPlainObject(@NonNull MedicineTakingEventData data) {
        MasterEventData masterEventData = data.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        MedicineTakingData medicineTakingData = data.getMedicineTaking();
        MedicineTaking medicineTaking = medicineTakingData == null ? null : medicineTakingMapper.mapToPlainObject(medicineTakingData);
        return MedicineTakingEvent.builder()
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
                .medicineTaking(medicineTaking)
                .build();
    }

    @Override
    public MedicineTakingEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                 @NonNull MedicineTakingEvent medicineTakingEvent) {
        MedicineTakingEventEntity medicineTakingEventEntity;
        if (medicineTakingEvent.getId() == null) {
            medicineTakingEventEntity = new MedicineTakingEventEntity();
        } else {
            medicineTakingEventEntity = (MedicineTakingEventEntity) blockingEntityStore.findByKey(MedicineTakingEvent.class, medicineTakingEvent.getId());
        }
        fillNonReferencedFields(medicineTakingEventEntity, medicineTakingEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, medicineTakingEvent.getMasterEventId());
        medicineTakingEventEntity.setMasterEvent(masterEventEntity);

        MedicineTaking medicineTaking = medicineTakingEvent.getMedicineTaking();
        if (medicineTaking != null) {
            MedicineTakingEntity medicineTakingEntity = (MedicineTakingEntity) blockingEntityStore.findByKey(MedicineTakingEntity.class, medicineTaking.getId());
            medicineTakingEventEntity.setMedicineTaking(medicineTakingEntity);
        }
        return medicineTakingEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull MedicineTakingEventEntity to, @NonNull MedicineTakingEvent from) {
    }
}
