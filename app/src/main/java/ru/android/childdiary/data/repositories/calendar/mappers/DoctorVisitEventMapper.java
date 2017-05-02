package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventData;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

public class DoctorVisitEventMapper {
    public static DoctorVisitEvent mapToPlainObject(@NonNull DoctorVisitEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        DoctorVisitData doctorVisitData = eventData.getDoctorVisit();
        DoctorVisit doctorVisit = doctorVisitData == null ? null : DoctorVisitMapper.mapToPlainObject(doctorVisitData);
        return DoctorVisitEvent.builder()
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
                .doctorVisit(doctorVisit)
                .build();
    }

    public static DoctorVisitEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                     @NonNull DoctorVisitEvent doctorVisitEvent) {
        return mapToEntity(blockingEntityStore, doctorVisitEvent, null);
    }

    public static DoctorVisitEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                                     @NonNull DoctorVisitEvent doctorVisitEvent,
                                                     @Nullable MasterEvent masterEvent) {
        DoctorVisitEventEntity doctorVisitEventEntity;
        if (doctorVisitEvent.getId() == null) {
            doctorVisitEventEntity = new DoctorVisitEventEntity();
        } else {
            doctorVisitEventEntity = (DoctorVisitEventEntity) blockingEntityStore.findByKey(DoctorVisitEvent.class, doctorVisitEvent.getId());
        }
        fillNonReferencedFields(doctorVisitEventEntity, doctorVisitEvent);
        if (masterEvent != null) {
            MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, masterEvent.getMasterEventId());
            doctorVisitEventEntity.setMasterEvent(masterEventEntity);
        }
        DoctorVisit doctorVisit = doctorVisitEvent.getDoctorVisit();
        if (doctorVisit != null) {
            DoctorVisitEntity doctorVisitEntity = (DoctorVisitEntity) blockingEntityStore.findByKey(DoctorVisitEntity.class, doctorVisit.getId());
            doctorVisitEventEntity.setDoctorVisit(doctorVisitEntity);
        }
        return doctorVisitEventEntity;
    }

    private static void fillNonReferencedFields(@NonNull DoctorVisitEventEntity to, @NonNull DoctorVisitEvent from) {
    }
}
