package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.calendar.core.RepeatParametersData;
import ru.android.childdiary.data.db.entities.calendar.core.RepeatParametersEntity;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorData;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.db.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.db.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.data.repositories.dictionaries.doctors.mappers.DoctorMapper;
import ru.android.childdiary.domain.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.medical.data.DoctorVisit;

public class DoctorVisitMapper implements EntityMapper<DoctorVisitData, DoctorVisitEntity, DoctorVisit> {
    private final ChildMapper childMapper;
    private final DoctorMapper doctorMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public DoctorVisitMapper(ChildMapper childMapper,
                             DoctorMapper doctorMapper,
                             RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.doctorMapper = doctorMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public DoctorVisit mapToPlainObject(@NonNull DoctorVisitData doctorVisitData) {
        ChildData childData = doctorVisitData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        DoctorData doctorData = doctorVisitData.getDoctor();
        Doctor doctor = doctorData == null ? null : doctorMapper.mapToPlainObject(doctorData);
        RepeatParametersData repeatParametersData = doctorVisitData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return DoctorVisit.builder()
                .id(doctorVisitData.getId())
                .child(child)
                .doctor(doctor)
                .repeatParameters(repeatParameters)
                .name(doctorVisitData.getName())
                .durationInMinutes(doctorVisitData.getDurationInMinutes())
                .dateTime(doctorVisitData.getDateTime())
                .finishDateTime(doctorVisitData.getFinishDateTime())
                .isExported(doctorVisitData.isExported())
                .notifyTimeInMinutes(doctorVisitData.getNotifyTimeInMinutes())
                .note(doctorVisitData.getNote())
                .imageFileName(doctorVisitData.getImageFileName())
                .isDeleted(doctorVisitData.isDeleted())
                .build();
    }

    @Override
    public DoctorVisitEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                         @NonNull DoctorVisit doctorVisit) {
        DoctorVisitEntity doctorVisitEntity;
        if (doctorVisit.getId() == null) {
            doctorVisitEntity = new DoctorVisitEntity();
        } else {
            doctorVisitEntity = (DoctorVisitEntity) blockingEntityStore.findByKey(DoctorVisitEntity.class, doctorVisit.getId());
        }
        fillNonReferencedFields(doctorVisitEntity, doctorVisit);
        Child child = doctorVisit.getChild();
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            doctorVisitEntity.setChild(childEntity);
        }
        Doctor doctor = doctorVisit.getDoctor();
        if (doctor != null) {
            DoctorEntity doctorEntity = (DoctorEntity) blockingEntityStore.findByKey(DoctorEntity.class, doctor.getId());
            doctorVisitEntity.setDoctor(doctorEntity);
        }
        RepeatParameters repeatParameters = doctorVisit.getRepeatParameters();
        if (repeatParameters != null) {
            RepeatParametersEntity repeatParametersEntity = (RepeatParametersEntity) blockingEntityStore.findByKey(RepeatParametersEntity.class, repeatParameters.getId());
            doctorVisitEntity.setRepeatParameters(repeatParametersEntity);
        }
        return doctorVisitEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull DoctorVisitEntity to, @NonNull DoctorVisit from) {
        to.setName(from.getName());
        to.setDurationInMinutes(from.getDurationInMinutes());
        to.setDateTime(from.getDateTime());
        to.setFinishDateTime(from.getFinishDateTime());
        to.setExported(from.getIsExported());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
        to.setDeleted(from.getIsDeleted());
    }
}
