package ru.android.healthvector.domain.exercises.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.core.validation.EventFieldType;
import ru.android.healthvector.domain.core.validation.EventValidationException;
import ru.android.healthvector.domain.core.validation.EventValidationResult;
import ru.android.healthvector.domain.core.validation.EventValidator;
import ru.android.healthvector.domain.core.validation.core.ValidationException;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.utils.ObjectUtils;

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
            result.addMessage(context.getString(R.string.enter_exercise_name));
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
