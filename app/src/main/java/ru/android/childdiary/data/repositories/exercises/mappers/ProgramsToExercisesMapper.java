package ru.android.childdiary.data.repositories.exercises.mappers;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.dto.Program;
import ru.android.childdiary.data.dto.Programs;
import ru.android.childdiary.data.repositories.core.mappers.Mapper;
import ru.android.childdiary.domain.interactors.exercises.Exercise;

public class ProgramsToExercisesMapper implements Mapper<Programs, List<Exercise>> {
    private final Mapper<Program, Exercise> mapper;

    @Inject
    public ProgramsToExercisesMapper(ProgramToExerciseMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Exercise> map(@NonNull Programs programs) {
        return Observable.fromIterable(programs.getPrograms())
                .map(mapper::map)
                .toList()
                .blockingGet();
    }
}
