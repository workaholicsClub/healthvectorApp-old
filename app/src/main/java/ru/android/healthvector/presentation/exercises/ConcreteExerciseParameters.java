package ru.android.healthvector.presentation.exercises;

import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;

@Value
@Builder
public class ConcreteExerciseParameters {
    ConcreteExercise defaultConcreteExercise;
    LocalTime startTime;
    LocalTime finishTime;
}
