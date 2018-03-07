package ru.android.healthvector.data.repositories.exercises.mappers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.healthvector.data.network.dto.Program;
import ru.android.healthvector.data.network.dto.Programs;
import ru.android.healthvector.data.repositories.core.mappers.Mapper;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.utils.ObjectUtils;

public class ProgramsToExercisesMapper implements Mapper<Programs, List<Exercise>> {
    @Inject
    public ProgramsToExercisesMapper() {
    }

    @Override
    public List<Exercise> map(@NonNull Programs programs) {
        List<Exercise> exercises = new ArrayList<>();
        for (Program program : programs.getPrograms()) {
            int exerciseIndex = findExerciseIndex(exercises, program.getServerId());
            if (exerciseIndex == -1) {
                Exercise exercise = createExercise(program);
                exercises.add(exercise);
            } else {
                Exercise exercise = exercises.get(exerciseIndex);
                exercise = addLocalization(exercise, program);
                exercises.set(exerciseIndex, exercise);
            }
        }
        return exercises;
    }

    private int findExerciseIndex(List<Exercise> exercises, Long serverId) {
        for (int i = 0; i < exercises.size(); ++i) {
            if (ObjectUtils.equals(exercises.get(i).getServerId(), serverId)) {
                return i;
            }
        }
        return -1;
    }

    private Exercise createExercise(Program program) {
        return Exercise.builder()
                .id(null)
                .serverId(program.getServerId())
                .code(program.getCode())
                .nameEn(program.isEn() ? program.getName() : null)
                .nameRu(program.isRu() ? program.getName() : null)
                .descriptionEn(program.isEn() ? program.getDescription() : null)
                .descriptionRu(program.isRu() ? program.getDescription() : null)
                .build();
    }

    private Exercise addLocalization(Exercise exercise, Program program) {
        Exercise.ExerciseBuilder builder = exercise.toBuilder();

        if (TextUtils.isEmpty(exercise.getCode())) {
            builder.code(program.getCode());
        }
        if (program.isRu()) {
            if (TextUtils.isEmpty(exercise.getNameRu())) {
                builder.nameRu(program.getName());
            }
            if (TextUtils.isEmpty(exercise.getDescriptionRu())) {
                builder.descriptionRu(program.getDescription());
            }
        } else if (program.isEn()) {
            if (TextUtils.isEmpty(exercise.getNameEn())) {
                builder.nameEn(program.getName());
            }
            if (TextUtils.isEmpty(exercise.getDescriptionEn())) {
                builder.descriptionEn(program.getDescription());
            }
        }

        return builder.build();
    }
}
