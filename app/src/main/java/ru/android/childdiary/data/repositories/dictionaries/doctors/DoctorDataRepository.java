package ru.android.childdiary.data.repositories.dictionaries.doctors;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.data.Doctor;

@Singleton
public class DoctorDataRepository extends CrudDataRepository<Doctor> {
    @Inject
    public DoctorDataRepository(DoctorDbService dbService) {
        super(dbService);
    }
}