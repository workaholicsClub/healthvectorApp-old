package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.SleepEventData;
import ru.android.healthvector.data.db.entities.calendar.standard.SleepEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;
import ru.android.healthvector.domain.child.data.Child;

public class SleepEventMapper implements EntityMapper<SleepEventData, SleepEventEntity, SleepEvent> {
    private final ChildMapper childMapper;

    @Inject
    public SleepEventMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public SleepEvent mapToPlainObject(@NonNull SleepEventData sleepEventData) {
        MasterEventData masterEventData = sleepEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return SleepEvent.builder()
                .id(sleepEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyDateTime(masterEventData.getNotifyDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
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
