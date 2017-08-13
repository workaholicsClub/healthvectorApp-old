package ru.android.childdiary.data.repositories.core.generators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.entities.calendar.ExerciseEventEntity;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.ExerciseEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.data.repositories.core.generators.EventsGenerator;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.exercises.data.ConcreteExercise;

public class ExerciseEventsGenerator extends EventsGenerator<ConcreteExercise> {
    private final MasterEventMapper masterEventMapper;
    private final ExerciseEventMapper exerciseEventMapper;
    private List<MasterEventEntity> masterEvents;
    private List<ExerciseEventEntity> events;

    @Inject
    public ExerciseEventsGenerator(ReactiveEntityStore<Persistable> dataStore,
                                   MasterEventMapper masterEventMapper,
                                   ExerciseEventMapper exerciseEventMapper) {
        super(dataStore);
        this.masterEventMapper = masterEventMapper;
        this.exerciseEventMapper = exerciseEventMapper;
    }

    @Override
    protected void startInsertion() {
        masterEvents = new ArrayList<>();
        events = new ArrayList<>();
    }

    @Override
    protected void createEvent(@NonNull ConcreteExercise concreteExercise,
                               @NonNull DateTime dateTime,
                               @Nullable Integer linearGroup) {
        ExerciseEvent event = ExerciseEvent.builder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.EXERCISE)
                .dateTime(dateTime)
                .notifyTimeInMinutes(concreteExercise.getNotifyTimeInMinutes())
                .note(null)
                .isDone(null)
                .child(concreteExercise.getChild())
                .linearGroup(linearGroup)
                .concreteExercise(concreteExercise)
                .name(concreteExercise.getName())
                .durationInMinutes(concreteExercise.getDurationInMinutes())
                .imageFileName(null)
                .build();
        MasterEventEntity masterEventEntity = masterEventMapper.mapToEntity(blockingEntityStore, event);
        ExerciseEventEntity eventEntity = exerciseEventMapper.mapToEntity(blockingEntityStore, event);
        eventEntity.setMasterEvent(masterEventEntity);
        masterEvents.add(masterEventEntity);
        events.add(eventEntity);
    }

    @Override
    protected void finishInsertion() {
        blockingEntityStore.insert(masterEvents);
        blockingEntityStore.insert(events);
    }
}
