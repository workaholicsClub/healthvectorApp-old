package ru.android.childdiary.data.repositories.exercises.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.core.RepeatParametersEntity;
import ru.android.childdiary.data.entities.exercises.ConcreteExerciseData;
import ru.android.childdiary.data.entities.exercises.ConcreteExerciseEntity;
import ru.android.childdiary.data.entities.exercises.ExerciseData;
import ru.android.childdiary.data.entities.exercises.ExerciseEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.domain.interactors.exercises.Exercise;

public class ConcreteExerciseMapper implements EntityMapper<ConcreteExerciseData, ConcreteExerciseEntity, ConcreteExercise> {
    private final ChildMapper childMapper;
    private final ExerciseMapper exerciseMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public ConcreteExerciseMapper(ChildMapper childMapper,
                                  ExerciseMapper exerciseMapper,
                                  RepeatParametersMapper repeatParametersMapper) {
        this.childMapper = childMapper;
        this.exerciseMapper = exerciseMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    @Override
    public ConcreteExercise mapToPlainObject(@NonNull ConcreteExerciseData concreteExerciseData) {
        ChildData childData = concreteExerciseData.getChild();
        Child child = childData == null ? null : childMapper.mapToPlainObject(childData);
        ExerciseData exerciseData = concreteExerciseData.getExercise();
        Exercise exercise = exerciseData == null ? null : exerciseMapper.mapToPlainObject(exerciseData);
        RepeatParametersData repeatParametersData = concreteExerciseData.getRepeatParameters();
        RepeatParameters repeatParameters = repeatParametersData == null ? null : repeatParametersMapper.mapToPlainObject(repeatParametersData);
        return ConcreteExercise.builder()
                .id(concreteExerciseData.getId())
                .child(child)
                .exercise(exercise)
                .repeatParameters(repeatParameters)
                .name(concreteExerciseData.getName())
                .durationInMinutes(concreteExerciseData.getDurationInMinutes())
                .dateTime(concreteExerciseData.getDateTime())
                .finishDateTime(concreteExerciseData.getFinishDateTime())
                .isExported(concreteExerciseData.isExported())
                .notifyTimeInMinutes(concreteExerciseData.getNotifyTimeInMinutes())
                .note(concreteExerciseData.getNote())
                .imageFileName(concreteExerciseData.getImageFileName())
                .isDeleted(concreteExerciseData.isDeleted())
                .build();
    }

    @Override
    public ConcreteExerciseEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                              @NonNull ConcreteExercise concreteExercise) {
        ConcreteExerciseEntity concreteExerciseEntity;
        if (concreteExercise.getId() == null) {
            concreteExerciseEntity = new ConcreteExerciseEntity();
        } else {
            concreteExerciseEntity = (ConcreteExerciseEntity) blockingEntityStore.findByKey(ConcreteExerciseEntity.class, concreteExercise.getId());
        }
        fillNonReferencedFields(concreteExerciseEntity, concreteExercise);
        Child child = concreteExercise.getChild();
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            concreteExerciseEntity.setChild(childEntity);
        }
        Exercise exercise = concreteExercise.getExercise();
        if (exercise != null) {
            ExerciseEntity exerciseEntity = (ExerciseEntity) blockingEntityStore.findByKey(ExerciseEntity.class, exercise.getId());
            concreteExerciseEntity.setExercise(exerciseEntity);
        }
        RepeatParameters repeatParameters = concreteExercise.getRepeatParameters();
        if (repeatParameters != null) {
            RepeatParametersEntity repeatParametersEntity = (RepeatParametersEntity) blockingEntityStore.findByKey(RepeatParametersEntity.class, repeatParameters.getId());
            concreteExerciseEntity.setRepeatParameters(repeatParametersEntity);
        }
        return concreteExerciseEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ConcreteExerciseEntity to, @NonNull ConcreteExercise from) {
        to.setName(from.getName());
        to.setDurationInMinutes(from.getDurationInMinutes());
        to.setDateTime(from.getDateTime());
        to.setFinishDateTime(from.getFinishDateTime());
        to.setExported(from.getIsExported());
        to.setNotifyTimeInMinutes(from.getNotifyTimeInMinutes());
        to.setNote(from.getNote());
        to.setImageFileName(from.getImageFileName());
        to.setDeleted(from.getIsDeleted());
    }
}
