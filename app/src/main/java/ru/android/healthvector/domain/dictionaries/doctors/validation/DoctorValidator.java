package ru.android.healthvector.domain.dictionaries.doctors.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.dictionaries.doctors.DoctorDataRepository;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidator;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;

public class DoctorValidator extends DictionaryValidator<Doctor> {
    @Inject
    public DoctorValidator(Context context, DoctorDataRepository repository) {
        super(context, repository);
    }

    @Override
    protected String getName(@NonNull Doctor item) {
        return item.getName();
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.DOCTOR_NAME;
    }

    @Override
    protected String getEmptyNameMessage() {
        return context.getString(R.string.enter_doctor_name);
    }
}
