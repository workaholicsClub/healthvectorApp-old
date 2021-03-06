package ru.android.healthvector.presentation.exercises;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.exercises.data.Exercise;

@Value
@Builder(toBuilder = true)
public class ExerciseDetailState implements Serializable {
    @NonNull
    Child child;
    @NonNull
    Exercise exercise;
}
