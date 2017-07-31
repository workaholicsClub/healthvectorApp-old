package ru.android.childdiary.domain.interactors.exercises.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.EventFieldType;
import ru.android.childdiary.domain.core.validation.EventValidationException;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.core.validation.EventValidator;
import ru.android.childdiary.domain.core.validation.ValidationException;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.utils.ObjectUtils;

public class ConcreteExerciseValidator extends EventValidator<ConcreteExercise> {
    @Inject
    public ConcreteExerciseValidator(Context context) {
        super(context);
    }

    @Override
    public List<EventValidationResult> validate(@NonNull ConcreteExercise concreteExercise) {
        List<EventValidationResult> results = new ArrayList<>();

        EventValidationResult result;

        result = new EventValidationResult(EventFieldType.CONCRETE_EXERCISE_NAME);
        if (TextUtils.isEmpty(concreteExercise.getName())) {
            result.addMessage(context.getString(R.string.validate_concrete_exercise_name_empty));
        }
        results.add(result);

        if (ObjectUtils.isTrue(concreteExercise.getIsExported())) {
            validate(results, concreteExercise.getRepeatParameters());
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<EventValidationResult> results) {
        return new EventValidationException(results);
    }
}
