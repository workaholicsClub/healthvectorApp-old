package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.db.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.db.entities.calendar.events.standard.DiaperEventData;
import ru.android.childdiary.data.db.entities.calendar.events.standard.DiaperEventEntity;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public class DiaperEventMapper implements EntityMapper<DiaperEventData, DiaperEventEntity, DiaperEvent> {
    private final ChildMapper childMapper;

    @Inject
    public DiaperEventMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public DiaperEvent mapToPlainObject(@NonNull DiaperEventData diaperEventData) {
        MasterEventData masterEventData = diaperEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        return DiaperEvent.builder()
                .id(diaperEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .diaperState(diaperEventData.getDiaperState())
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
