package ru.android.healthvector.data.repositories.dictionaries.doctors;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.healthvector.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;

@Singleton
public class DoctorDataRepository extends CrudDataRepository<Doctor> {
    @Inject
    public DoctorDataRepository(DoctorDbService dbService) {
        super(dbService);
    }
}
