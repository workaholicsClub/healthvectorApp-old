package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.ExerciseEventData;
import ru.android.healthvector.data.db.entities.calendar.ExerciseEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.exercises.ConcreteExerciseData;
import ru.android.healthvector.data.db.entities.exercises.ConcreteExerciseEntity;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.data.repositories.exercises.mappers.ConcreteExerciseMapper;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;

public class ExerciseEventMapper implements EntityMapper<ExerciseEventData, ExerciseEventEntity, ExerciseEvent> {
    private final ChildMapper childMapper;
    private final ConcreteExerciseMapper concreteExerciseMapper;

    @Inject
    public ExerciseEventMapper(ChildMapper childMapper,
                               ConcreteExerciseMapper concreteExerciseMapper) {
        this.childMapper = childMapper;
        this.concreteExerciseMapper = concreteExerciseMapper;
    }

    @Override
    public ExerciseEvent mapToPlainObject(@NonNull ExerciseEventData exerciseEventData) {
        MasterEventData masterEventData = exerciseEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        ConcreteExerciseData concreteExerciseData = exerciseEventData.getConcreteExercise();
        ConcreteExercise concreteExercise = concreteExerciseData == null ? null : concreteExerciseMapper.mapToPlainObject(concreteExerciseData);
        return ExerciseEvent.builder()
                .id(exerciseEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyDateTime(masterEventData.getNotifyDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .concreteExercise(concreteExercise)
                .name(exerciseEventData.getName())
                .durationInMinutes(exerciseEventData.getDurationInMinutes())
                .imageFileName(exerciseEventData.getImageFileName())
                .build();
    }

    @Override
    public ExerciseEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                           @NonNull ExerciseEvent exerciseEvent) {
        ExerciseEventEntity exerciseEventEntity;
        if (exerciseEvent.getId() == null) {
            exerciseEventEntity = new ExerciseEventEntity();
        } else {
            exerciseEventEntity = (ExerciseEventEntity) blockingEntityStore.findByKey(ExerciseEventEntity.class, exerciseEvent.getId());
        }
        fillNonReferencedFields(exerciseEventEntity, exerciseEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, exerciseEvent.getMasterEventId());
        exerciseEventEntity.setMasterEvent(masterEventEntity);

        ConcreteExercise concreteExercise = exerciseEvent.getConcreteExercise();
        if (concreteExercise != null) {
            ConcreteExerciseEntity concreteExerciseEntity = (ConcreteExerciseEntity) blockingEntityStore.findByKey(ConcreteExerciseEntity.class, concreteExercise.getId());
            exerciseEventEntity.setConcreteExercise(concreteExerciseEntity);
        }
        return exerciseEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull ExerciseEventEntity to, @NonNull ExerciseEvent from) {
        to.setName(from.getName());
        to.setDurationInMinutes(from.getDurationInMinutes());
        to.setImageFileName(from.getImageFileName());
    }
}
