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
import ru.android.childdiary.utils.CollectionUtils;

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
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, exerciseMapper));
    }

    public Observable<List<Exercise>> putExercises(@NonNull List<Exercise> exercises) {
        return mapToEntities(exercises)
                .flatMap(this::insert)
                .flatMap(this::mapToExercises);
    }

    private Observable<List<ExerciseEntity>> insert(@NonNull List<ExerciseEntity> entities) {
        return dataStore.insert(entities)
                .map(CollectionUtils::toList)
                .toObservable()
                .doOnError(throwable -> logger.error("error on inserting exercise entities", throwable))
                .onErrorResumeNext(Observable.just(entities));
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
