package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.utils.ObjectUtils;

abstract class MedicalValidator<T> extends Validator<T, MedicalValidationResult> {
    protected final Context context;

    public MedicalValidator(Context context) {
        this.context = context;
    }

    protected void validate(List<MedicalValidationResult> results, @Nullable RepeatParameters repeatParameters) {
        PeriodicityType periodicityType = repeatParameters == null ? null : repeatParameters.getPeriodicity();
        LengthValue lengthValue = repeatParameters == null ? null : repeatParameters.getLength();
        validate(results, periodicityType);
        validate(results, lengthValue);
    }

    private void validate(List<MedicalValidationResult> results, @Nullable PeriodicityType periodicityType) {
        if (periodicityType == null) {
            MedicalValidationResult result = new MedicalValidationResult();
            result.addMessage(context.getString(R.string.validate_repeat_parameters_periodicity_empty));
            results.add(result);
        }
    }

    private void validate(List<MedicalValidationResult> results, @Nullable LengthValue lengthValue) {
        if (ObjectUtils.isEmpty(lengthValue)) {
            MedicalValidationResult result = new MedicalValidationResult();
            result.addMessage(context.getString(R.string.validate_repeat_parameters_length_empty));
            results.add(result);
        }
    }
}
