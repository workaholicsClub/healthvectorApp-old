package ru.android.childdiary.domain.dictionaries.medicinemeasure.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.dictionaries.medicinemeasure.MedicineMeasureDataRepository;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryValidator;
import ru.android.childdiary.domain.dictionaries.medicinemeasure.data.MedicineMeasure;

public class MedicineMeasureValidator extends DictionaryValidator<MedicineMeasure> {
    @Inject
    public MedicineMeasureValidator(Context context, MedicineMeasureDataRepository repository) {
        super(context, repository);
    }

    @Override
    protected String getName(@NonNull MedicineMeasure item) {
        return item.getName();
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.MEDICINE_MEASURE_NAME;
    }

    @Override
    protected String getEmptyNameMessage() {
        return context.getString(R.string.enter_measure_name);
    }
}
