package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.core.DoctorData;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorMapper {
    public static Doctor mapToPlainObject(@NonNull DoctorData doctorData) {
        return Doctor.builder()
                .id(doctorData.getId())
                .name(doctorData.getName())
                .build();
    }

    public static DoctorEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                           @NonNull Doctor doctor) {
        DoctorEntity doctorEntity;
        if (doctor.getId() == null) {
            doctorEntity = new DoctorEntity();
        } else {
            doctorEntity = (DoctorEntity) blockingEntityStore.findByKey(DoctorEntity.class, doctor.getId());
        }
        fillNonReferencedFields(doctorEntity, doctor);
        return doctorEntity;
    }

    private static void fillNonReferencedFields(@NonNull DoctorEntity to, @NonNull Doctor from) {
        to.setName(from.getName());
    }
}
