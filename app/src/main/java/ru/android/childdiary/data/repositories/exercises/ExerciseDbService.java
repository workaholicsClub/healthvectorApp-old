package ru.android.childdiary.data.repositories.exercises;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.exercises.ExerciseEntity;
import ru.android.childdiary.data.repositories.exercises.mappers.ExerciseMapper;
import ru.android.childdiary.domain.interactors.exercises.Exercise;

@Singleton
public class ExerciseDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final ExerciseMapper exerciseMapper;

    @Inject
    public ExerciseDbService(ReactiveEntityStore<Persistable> dataStore,
                             ExerciseMapper exerciseMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.exerciseMapper = exerciseMapper;
    }

    public Observable<List<Exercise>> getExercises() {
        return dataStore.select(ExerciseEntity.class)
                .orderBy(ExerciseEntity.ORDER_NUMBER, ExerciseEntity.NAME, ExerciseEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, exerciseMapper));
    }

    public Observable<List<Exercise>> putExercises(@NonNull List<Exercise> exercises) {
        return upsertFromRemoteSource(exercises).flatMap(this::mapToExercises);
    }

    private Observable<List<ExerciseEntity>> upsertFromRemoteSource(@NonNull List<Exercise> exercises) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {

            // запоминаем порядок, в котором объекты пришли с сервера
            // удаленные на сервере объекты не удаляем локально; либо нужна проверка, что на них никто больше не ссылается
            // принимаем, что уникальность объектов, приходящих с сервера гарантируется атрибутом SERVER_ID

            int i = 0;

            for (Exercise exercise : exercises) {
                ExerciseEntity exerciseEntity = blockingEntityStore.select(ExerciseEntity.class)
                        .where(ExerciseEntity.SERVER_ID.eq(exercise.getServerId()))
                        .get()
                        .firstOrNull();

                if (exerciseEntity == null) {
                    // добавлено новое занятие
                    exerciseEntity = exerciseMapper.mapToEntity(blockingEntityStore, exercise);
                    exerciseEntity.setOrderNumber(i);
                    blockingEntityStore.insert(exerciseEntity);
                } else {
                    // обновляем добавленное ранее занятие
                    Long id = exerciseEntity.getId();
                    exercise = exercise.toBuilder().id(id).build();
                    exerciseEntity = exerciseMapper.mapToEntity(blockingEntityStore, exercise);
                    exerciseEntity.setOrderNumber(i);
                    blockingEntityStore.update(exerciseEntity);
                }

                ++i;
            }

            List<ExerciseEntity> exerciseEntities = blockingEntityStore.select(ExerciseEntity.class)
                    .orderBy(ExerciseEntity.ORDER_NUMBER, ExerciseEntity.NAME, ExerciseEntity.ID)
                    .get().toList();
            return exerciseEntities;

        }));
    }

    private Observable<List<Exercise>> mapToExercises(@NonNull List<ExerciseEntity> entities) {
        return Observable.fromIterable(entities)
                .map(exerciseMapper::mapToPlainObject)
                .toList()
                .toObservable();
    }

    private Observable<List<ExerciseEntity>> mapToEntities(@NonNull List<Exercise> exercises) {
        return Observable.fromIterable(exercises)
                .map(exercise -> exerciseMapper.mapToEntity(blockingEntityStore, exercise))
                .toList()
                .toObservable();
    }
}
