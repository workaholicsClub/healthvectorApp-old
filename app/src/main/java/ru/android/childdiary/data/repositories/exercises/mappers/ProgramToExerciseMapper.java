package ru.android.childdiary.data.repositories.exercises.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.childdiary.data.dto.Program;
import ru.android.childdiary.data.repositories.core.mappers.Mapper;
import ru.android.childdiary.domain.interactors.exercises.Exercise;

public class ProgramToExerciseMapper implements Mapper<Program, Exercise> {
    @Inject
    public ProgramToExerciseMapper() {
    }

    @Override
    public Exercise map(@NonNull Program program) {
        return Exercise.builder()
                .id(null)
                .serverId(program.getServerId())
                .code(program.getCode())
                .name(program.getName())
                .description(program.getDescription())
                .build();
    }
}
