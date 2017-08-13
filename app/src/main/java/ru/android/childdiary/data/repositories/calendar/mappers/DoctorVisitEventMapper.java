package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.calendar.DoctorVisitEventData;
import ru.android.childdiary.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventData;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorData;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.db.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.db.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.dictionaries.doctors.mappers.DoctorMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.medical.data.DoctorVisit;

public class DoctorVisitEventMapper implements EntityMapper<DoctorVisitEventData, DoctorVisitEventEntity, DoctorVisitEvent> {
    private final ChildMapper childMapper;
    private final DoctorVisitMapper doctorVisitMapper;
    private final DoctorMapper doctorMapper;

    @Inject
    public DoctorVisitEventMapper(ChildMapper childMapper,
                                  DoctorVisitMapper doctorVisitMapper,
                                  DoctorMapper doctorMapper) {
        this.childMapper = childMapper;
        this.doctorVisitMapper = doctorVisitMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorVisitEvent mapToPlainObject(@NonNull DoctorVisitEventData doctorVisitEventData) {
        MasterEventData masterEventData = doctorVisitEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
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
            doctorVisitEventEntity = (DoctorVisitEventEntity) blockingEntityStore.findByKey(DoctorVisitEventEntity.class, doctorVisitEvent.getId());
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
