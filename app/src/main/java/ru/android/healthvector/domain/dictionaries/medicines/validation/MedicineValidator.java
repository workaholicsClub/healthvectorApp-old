package ru.android.healthvector.domain.dictionaries.medicines.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.dictionaries.medicines.MedicineDataRepository;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidator;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;

public class MedicineValidator extends DictionaryValidator<Medicine> {
    @Inject
    public MedicineValidator(Context context, MedicineDataRepository repository) {
        super(context, repository);
    }

    @Override
    protected String getName(@NonNull Medicine item) {
        return item.getName();
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.MEDICINE_NAME;
    }

    @Override
    protected String getEmptyNameMessage() {
        return context.getString(R.string.enter_medicine_name);
    }
}
