package ru.android.childdiary.domain.interactors.core.validation;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.interactors.calendar.data.core.LinearGroups;
import ru.android.childdiary.domain.interactors.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.validation.core.Validator;
import ru.android.childdiary.utils.ObjectUtils;

public abstract class EventValidator<T> extends Validator<T, EventValidationResult> {
    protected final Context context;

    public EventValidator(Context context) {
        this.context = context;
    }

    protected void validate(List<EventValidationResult> results, @Nullable RepeatParameters repeatParameters) {
        LinearGroups linearGroups = repeatParameters == null ? null : repeatParameters.getFrequency();
        PeriodicityType periodicityType = repeatParameters == null ? null : repeatParameters.getPeriodicity();
        LengthValue lengthValue = repeatParameters == null ? null : repeatParameters.getLength();
        if (linearGroups == null || linearGroups.getTimes().size() > 0) {
            validate(results, periodicityType);
            validate(results, lengthValue);
        }
    }

    private void validate(List<EventValidationResult> results, @Nullable PeriodicityType periodicityType) {
        if (periodicityType == null) {
            EventValidationResult result = new EventValidationResult();
            result.addMessage(context.getString(R.string.validate_repeat_parameters_periodicity_empty));
            results.add(result);
        }
    }

    private void validate(List<EventValidationResult> results, @Nullable LengthValue lengthValue) {
        if (ObjectUtils.isEmpty(lengthValue)) {
            EventValidationResult result = new EventValidationResult();
            result.addMessage(context.getString(R.string.validate_repeat_parameters_length_empty));
            results.add(result);
        }
    }
}
