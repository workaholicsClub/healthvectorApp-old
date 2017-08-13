package ru.android.childdiary.domain.dictionaries.doctors;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.dictionaries.doctors.DoctorDataRepository;
import ru.android.childdiary.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.dictionaries.doctors.validation.DoctorValidator;

public class DoctorInteractor extends BaseDictionaryInteractor<Doctor> {
    @Inject
    public DoctorInteractor(DoctorDataRepository repository,
                            DoctorValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Doctor buildItem(String name) {
        return Doctor.builder().name(name).build();
    }
}
