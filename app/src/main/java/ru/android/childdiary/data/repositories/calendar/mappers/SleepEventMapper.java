package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.SleepEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class SleepEventMapper implements EntityMapper<SleepEventData, SleepEventEntity, SleepEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public SleepEventMapper(ChildMapper childMapper,
                            RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public SleepEvent mapToPlainObject(@NonNull SleepEventData sleepEventData) {
        MasterEventData masterEventData = sleepEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return SleepEvent.builder()
                .id(sleepEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .repeatParameters(repeatParameters)
                .linearGroup(masterEventData.getLinearGroup())
                .finishDateTime(sleepEventData.getFinishDateTime())
                .isTimerStarted(sleepEventData.isTimerStarted())
                .build();
    }

    @Override
    public SleepEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                        @NonNull SleepEvent sleepEvent) {
        SleepEventEntity sleepEventEntity;
        if (sleepEvent.getId() == null) {
            sleepEventEntity = new SleepEventEntity();
        } else {
            sleepEventEntity = (SleepEventEntity) blockingEntityStore.findByKey(SleepEventEntity.class, sleepEvent.getId());
        }
        fillNonReferencedFields(sleepEventEntity, sleepEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, sleepEvent.getMasterEventId());
        sleepEventEntity.setMasterEvent(masterEventEntity);

        return sleepEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull SleepEventEntity to, @NonNull SleepEvent from) {
        to.setFinishDateTime(from.getFinishDateTime());
        to.setTimerStarted(from.getIsTimerStarted());
    }
}
