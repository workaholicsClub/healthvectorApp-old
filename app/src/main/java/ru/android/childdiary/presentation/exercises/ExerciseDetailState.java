package ru.android.childdiary.presentation.exercises;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.exercises.Exercise;

@Value
@Builder(toBuilder = true)
public class ExerciseDetailState implements Serializable {
    @NonNull
    Child child;
    @NonNull
    Exercise exercise;
}
