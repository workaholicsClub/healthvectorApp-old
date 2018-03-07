package ru.android.healthvector.domain.dictionaries.doctors;

import javax.inject.Inject;

import ru.android.healthvector.data.repositories.dictionaries.doctors.DoctorDataRepository;
import ru.android.healthvector.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.dictionaries.doctors.validation.DoctorValidator;

public class DoctorInteractor extends BaseDictionaryInteractor<Doctor> {
    @Inject
    public DoctorInteractor(DoctorDataRepository repository,
                            DoctorValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Doctor buildItem(String name) {
        return Doctor.builder().nameUser(name).build();
    }
}
