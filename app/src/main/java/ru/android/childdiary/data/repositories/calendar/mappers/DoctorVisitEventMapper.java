package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventData;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorData;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorVisitEventMapper implements EntityMapper<DoctorVisitEventData, DoctorVisitEventEntity, DoctorVisitEvent> {
    private final ChildMapper childMapper;
    private final RepeatParametersMapper repeatParametersMapper;
    private final DoctorVisitMapper doctorVisitMapper;
    private final DoctorMapper doctorMapper;

    @Inject
    public DoctorVisitEventMapper(ChildMapper childMapper,
                                  RepeatParametersMapper repeatParametersMapper,
                                  DoctorVisitMapper doctorVisitMapper,
                                  DoctorMapper doctorMapper) {
        this.childMapper = childMapper;
        this.repeatParametersMapper = repeatParametersMapper;
        this.doctorVisitMapper = doctorVisitMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorVisitEvent mapToPlainObject(@NonNull DoctorVisitEventData doctorVisitEventData) {
        MasterEventData masterEventData = doctorVisitEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        RepeatParametersData repeatParametersData = masterEventData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        DoctorVisitData doctorVisitData = doctorVisitEventData.getDoctorVisit();
        DoctorVisit doctorVisit = doctorVisitData == null ? null : doctorVisitMapper.mapToPlainObject(doctorVisitData);
        DoctorData doctorData = doctorVisitEventData.getDoctor();
        Doctor doctor = doctorData == null ? null : doctorMapper.mapToPlainObject(doctorData);
        return DoctorVisitEvent.builder()
                .id(doctorVisitEventData.getId())
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
                .doctor(doctor)
                .name(doctorVisitEventData.getName())
                .durationInMinutes(doctorVisitEventData.getDurationInMinutes())
                .imageFileName(doctorVisitEventData.getImageFileName())
                .build();
    }

    @Override
    public DoctorVisitEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                              @NonNull DoctorVisitEvent doctorVisitEvent) {
        DoctorVisitEventEntity doctorVisitEventEntity;
        if (doctorVisitEvent.getId() == null) {
            doctorVisitEventEntity = new DoctorVisitEventEntity();
        } else {
            doctorVisitEventEntity = (DoctorVisitEventEntity) blockingEntityStore.findByKey(DoctorVisitEvent.class, doctorVisitEvent.getId());
        }
        fillNonReferencedFields(doctorVisitEventEntity, doctorVisitEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, doctorVisitEvent.getMasterEventId());
        doctorVisitEventEntity.setMasterEvent(masterEventEntity);

        DoctorVisit doctorVisit = doctorVisitEvent.getDoctorVisit();
        if (doctorVisit != null) {
            DoctorVisitEntity doctorVisitEntity = (DoctorVisitEntity) blockingEntityStore.findByKey(DoctorVisitEntity.class, doctorVisit.getId());
            doctorVisitEventEntity.setDoctorVisit(doctorVisitEntity);
        }
        Doctor doctor = doctorVisitEvent.getDoctor();
        if (doctor != null) {
            DoctorEntity doctorEntity = (DoctorEntity) blockingEntityStore.findByKey(DoctorEntity.class, doctor.getId());
            doctorVisitEventEntity.setDoctor(doctorEntity);
        }
        return doctorVisitEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull DoctorVisitEventEntity to, @NonNull DoctorVisitEvent from) {
        to.setName(from.getName());
        to.setDurationInMinutes(from.getDurationInMinutes());
        to.setImageFileName(from.getImageFileName());
    }
}
