package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersEntity;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorData;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorVisitMapper {
    public static DoctorVisit mapToPlainObject(@NonNull DoctorVisitData doctorVisitData) {
        ChildData childData = doctorVisitData.getChild();
        Child child = childData == null ? null : ChildMapper.mapToPlainObject(childData);
        DoctorData doctorData = doctorVisitData.getDoctor();
        Doctor doctor = doctorData == null ? null : DoctorMapper.mapToPlainObject(doctorData);
        RepeatParametersData repeatParametersData = doctorVisitData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : RepeatParametersMapper.mapToPlainObject(repeatParametersData);
        return DoctorVisit.builder()
                .id(doctorVisitData.getId())
                .child(child)
                .doctor(doctor)
                .repeatParameters(repeatParameters)
                .name(doctorVisitData.getName())
                .durationInMinutes(doctorVisitData.getDurationInMinutes())
                .dateTime(doctorVisitData.getDateTime())
                .notifyTimeInMinutes(doctorVisitData.getNotifyTimeInMinutes())
                .note(doctorVisitData.getNote())
                .imageFileName(doctorVisitData.getImageFileName())
                .build();
    }

    public static DoctorVisitEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    private static void fillNonReferencedFields(@NonNull DoctorVisitEntity to, @NonNull DoctorVisit from) {
        to.setName(from.getName());
        to.setDurationInMinutes(from.getDurationInMinutes());
        to.setDateTime(from.getDateTime());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
    }
}
