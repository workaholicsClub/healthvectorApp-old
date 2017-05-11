package ru.android.childdiary.data.repositories.medical.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.medical.core.DoctorData;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorMapper implements EntityMapper<DoctorData, DoctorEntity, Doctor> {
    @Inject
    public DoctorMapper() {
    }

    @Override
    public Doctor mapToPlainObject(@NonNull DoctorData data) {
        return Doctor.builder()
                .id(data.getId())
                .name(data.getName())
                .build();
    }

    @Override
    public DoctorEntity mapToEntity(BlockingEntityStore blockingEntityStore,
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

    @Override
    public void fillNonReferencedFields(@NonNull DoctorEntity to, @NonNull Doctor from) {
        to.setName(from.getName());
    }
}
