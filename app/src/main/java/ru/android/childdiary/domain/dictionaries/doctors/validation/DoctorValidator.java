package ru.android.childdiary.domain.dictionaries.doctors.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.dictionaries.doctors.DoctorDataRepository;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryValidator;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;

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
