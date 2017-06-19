package ru.android.childdiary.presentation.exercises;

import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;

@Value
@Builder
public class ConcreteExerciseParameters {
    ConcreteExercise defaultConcreteExercise;
    LocalTime startTime;
    LocalTime finishTime;
}
