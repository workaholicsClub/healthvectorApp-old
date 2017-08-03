package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationException;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.core.validation.EventValidator;
import ru.android.childdiary.domain.interactors.core.validation.ValidationException;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.utils.ObjectUtils;

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
