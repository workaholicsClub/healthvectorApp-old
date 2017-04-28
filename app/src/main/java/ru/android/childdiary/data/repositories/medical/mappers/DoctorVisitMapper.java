package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.DoctorVisitData;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorData;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorVisitMapper {
    public static DoctorVisit mapToPlainObject(@NonNull DoctorVisitData doctorVisitData) {
        DoctorData doctorData = doctorVisitData.getDoctor();
        Doctor doctor = doctorData == null ? null : DoctorMapper.mapToPlainObject(doctorData);
        return DoctorVisit.builder()
                .id(doctorVisitData.getId())
                .doctor(doctor)
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
        Doctor doctor = doctorVisit.getDoctor();
        if (doctor != null) {
            DoctorEntity doctorEntity = (DoctorEntity) blockingEntityStore.findByKey(DoctorEntity.class, doctor.getId());
            doctorVisitEntity.setDoctor(doctorEntity);
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
