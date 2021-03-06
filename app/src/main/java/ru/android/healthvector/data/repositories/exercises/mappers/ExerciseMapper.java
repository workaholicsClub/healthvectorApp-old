package ru.android.healthvector.data.repositories.exercises.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.exercises.ExerciseData;
import ru.android.healthvector.data.db.entities.exercises.ExerciseEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.exercises.data.Exercise;

public class ExerciseMapper implements EntityMapper<ExerciseData, ExerciseEntity, Exercise> {
    @Inject
    public ExerciseMapper() {
    }

    @Override
    public Exercise mapToPlainObject(@NonNull ExerciseData exerciseData) {
        return Exercise.builder()
                .id(exerciseData.getId())
                .serverId(exerciseData.getServerId())
                .code(exerciseData.getCode())
                .nameEn(exerciseData.getNameEn())
                .nameRu(exerciseData.getNameRu())
                .descriptionEn(exerciseData.getDescriptionEn())
                .descriptionRu(exerciseData.getDescriptionRu())
                .build();
    }

    @Override
    public ExerciseEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull Exercise exercise) {
        ExerciseEntity exerciseEntity;
        if (exercise.getId() == null) {
            exerciseEntity = new ExerciseEntity();
        } else {
            exerciseEntity = (ExerciseEntity) blockingEntityStore.findByKey(ExerciseEntity.class, exercise.getId());
        }
        fillNonReferencedFields(exerciseEntity, exercise);
        return exerciseEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ExerciseEntity to, @NonNull Exercise from) {
        to.setServerId(from.getServerId());
        to.setCode(from.getCode());
        to.setNameEn(from.getNameEn());
        to.setNameRu(from.getNameRu());
        to.setDescriptionEn(from.getDescriptionEn());
        to.setDescriptionRu(from.getDescriptionRu());
    }
}
