package ru.android.childdiary.data.repositories.exercises.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.childdiary.data.network.dto.Program;
import ru.android.childdiary.data.repositories.core.mappers.Mapper;
import ru.android.childdiary.domain.exercises.data.Exercise;

public class ProgramToExerciseMapper implements Mapper<Program, Exercise> {
    private static final String LANGUAGE_EN = "en", LANGUAGE_RU = "ru";

    @Inject
    public ProgramToExerciseMapper() {
    }

    @Override
    public Exercise map(@NonNull Program program) {
        boolean isEn = LANGUAGE_EN.equals(program.getLanguageCode());
        boolean isRu = LANGUAGE_RU.equals(program.getLanguageCode());
        return Exercise.builder()
                .id(null)
                .serverId(program.getServerId())
                .code(program.getCode())
                .nameEn(isEn ? program.getName() : null)
                .nameRu(isRu ? program.getName() : null)
                .descriptionEn(isEn ? program.getDescription() : null)
                .descriptionRu(isRu ? program.getDescription() : null)
                .build();
    }
}
