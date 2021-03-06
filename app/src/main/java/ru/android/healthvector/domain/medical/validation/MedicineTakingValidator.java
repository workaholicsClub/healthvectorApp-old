package ru.android.healthvector.domain.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.core.validation.EventValidationException;
import ru.android.healthvector.domain.core.validation.EventValidationResult;
import ru.android.healthvector.domain.core.validation.EventValidator;
import ru.android.healthvector.domain.core.validation.core.ValidationException;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.utils.ObjectUtils;

public class MedicineTakingValidator extends EventValidator<MedicineTaking> {
    @Inject
    public MedicineTakingValidator(Context context) {
        super(context);
    }

    @Override
    public List<EventValidationResult> validate(@NonNull MedicineTaking medicineTaking) {
        List<EventValidationResult> results = new ArrayList<>();

        Medicine medicine = medicineTaking.getMedicine();
        if (medicine == null || medicine.getId() == null) {
            EventValidationResult result = new EventValidationResult();
            result.addMessage(context.getString(R.string.validate_medicine_taking_medicine_empty));
            results.add(result);
        }

        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            validate(results, medicineTaking.getRepeatParameters());
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<EventValidationResult> results) {
        return new EventValidationException(results);
    }
}
