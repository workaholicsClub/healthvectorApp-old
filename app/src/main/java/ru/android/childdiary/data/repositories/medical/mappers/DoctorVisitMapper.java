package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.core.RepeatParametersEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorData;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

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
    public DoctorVisit mapToPlainObject(@NonNull DoctorVisitData data) {
        ChildData childData = data.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        DoctorData doctorData = data.getDoctor();
        Doctor doctor = doctorData == null ? null : doctorMapper.mapToPlainObject(doctorData);
        RepeatParametersData repeatParametersData = data.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return DoctorVisit.builder()
                .id(data.getId())
                .child(child)
                .doctor(doctor)
                .repeatParameters(repeatParameters)
                .name(data.getName())
                .durationInMinutes(data.getDurationInMinutes())
                .dateTime(data.getDateTime())
                .finishDateTime(data.getFinishDateTime())
                .exported(data.getExported())
                .notifyTimeInMinutes(data.getNotifyTimeInMinutes())
                .note(data.getNote())
                .imageFileName(data.getImageFileName())
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
        to.setExported(from.getExported());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
    }
}
