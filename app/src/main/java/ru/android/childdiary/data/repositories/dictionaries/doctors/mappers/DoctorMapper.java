package ru.android.childdiary.data.repositories.dictionaries.doctors.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorData;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;

public class DoctorMapper implements EntityMapper<DoctorData, DoctorEntity, Doctor> {
    @Inject
    public DoctorMapper() {
    }

    @Override
    public Doctor mapToPlainObject(@NonNull DoctorData doctorData) {
        return Doctor.builder()
                .id(doctorData.getId())
                .name(doctorData.getName())
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
