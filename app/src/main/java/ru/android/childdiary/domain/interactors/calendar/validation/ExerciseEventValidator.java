package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.validation.ValidationException;
import ru.android.childdiary.domain.interactors.core.validation.Validator;
import ru.android.childdiary.domain.interactors.calendar.data.ExerciseEvent;

public class ExerciseEventValidator extends Validator<ExerciseEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public ExerciseEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull ExerciseEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        CalendarValidationResult result;

        result = new CalendarValidationResult(CalendarFieldType.EXERCISE_EVENT_NAME);
        if (TextUtils.isEmpty(event.getName())) {
            result.addMessage(context.getString(R.string.validate_concrete_exercise_name_empty));
        }
        results.add(result);

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<CalendarValidationResult> results) {
        return new CalendarValidationException(results);
    }
}
