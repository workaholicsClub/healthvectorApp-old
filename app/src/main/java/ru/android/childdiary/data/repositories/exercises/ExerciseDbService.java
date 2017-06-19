package ru.android.childdiary.data.repositories.exercises;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.ExerciseEventEntity;
import ru.android.childdiary.data.entities.exercises.ConcreteExerciseEntity;
import ru.android.childdiary.data.entities.exercises.ExerciseEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.core.ExerciseEventsGenerator;
import ru.android.childdiary.data.repositories.exercises.mappers.ConcreteExerciseMapper;
import ru.android.childdiary.data.repositories.exercises.mappers.ExerciseMapper;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseResponse;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class ExerciseDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final ExerciseEventsGenerator eventsGenerator;
    private final ExerciseMapper exerciseMapper;
    private final ConcreteExerciseMapper concreteExerciseMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public ExerciseDbService(ReactiveEntityStore<Persistable> dataStore,
                             ExerciseEventsGenerator eventsGenerator,
                             ExerciseMapper exerciseMapper,
                             ConcreteExerciseMapper concreteExerciseMapper,
                             RepeatParametersMapper repeatParametersMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.eventsGenerator = eventsGenerator;
        this.exerciseMapper = exerciseMapper;
        this.concreteExerciseMapper = concreteExerciseMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    public Observable<Exercise> getExercise(@NonNull Exercise exercise) {
        return dataStore.select(ExerciseEntity.class)
                .where(ExerciseEntity.ID.eq(exercise.getId()))
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToObservable(reactiveResult, exerciseMapper));
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

    public Single<Boolean> hasConnectedEvents(@NonNull Exercise exercise) {
        return dataStore.count(ExerciseEntity.class)
                .join(ConcreteExerciseEntity.class).on(ExerciseEntity.ID.eq(ConcreteExerciseEntity.EXERCISE_ID))
                .join(ExerciseEventEntity.class).on(ConcreteExerciseEntity.ID.eq(ExerciseEventEntity.CONCRETE_EXERCISE_ID))
                .where(ExerciseEntity.ID.eq(exercise.getId()))
                .get()
                .single()
                .map(count -> count > 0);
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(blockingEntityStore, repeatParameters, repeatParametersMapper);
    }

    private ConcreteExercise insertConcreteExercise(@NonNull ConcreteExercise concreteExercise) {
        return DbUtils.insert(blockingEntityStore, concreteExercise, concreteExerciseMapper);
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(blockingEntityStore, repeatParameters, repeatParametersMapper);
    }

    private ConcreteExercise updateConcreteExercise(@NonNull ConcreteExercise concreteExercise) {
        return DbUtils.update(blockingEntityStore, concreteExercise, concreteExerciseMapper);
    }

    @Nullable
    private RepeatParameters upsertRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        if (repeatParameters != null) {
            if (repeatParameters.getId() == null) {
                return insertRepeatParameters(repeatParameters);
            } else {
                return updateRepeatParameters(repeatParameters);
            }
        }
        return null;
    }

    public Observable<UpsertConcreteExerciseResponse> add(@NonNull UpsertConcreteExerciseRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            ConcreteExercise concreteExercise = request.getConcreteExercise();
            RepeatParameters repeatParameters = upsertRepeatParameters(concreteExercise.getRepeatParameters());
            ConcreteExercise result = concreteExercise.toBuilder().repeatParameters(repeatParameters).build();
            result = insertConcreteExercise(result);

            boolean needToAddEvents = ObjectUtils.isTrue(result.getIsExported());

            int count = 0;
            if (needToAddEvents) {
                count = eventsGenerator.generateEvents(result);
            }

            return UpsertConcreteExerciseResponse.builder()
                    .request(request)
                    .addedEventsCount(count)
                    .concreteExercise(result)
                    .imageFilesToDelete(Collections.emptyList())
                    .build();
        }));
    }

    public Observable<UpsertConcreteExerciseResponse> update(@NonNull UpsertConcreteExerciseRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            ConcreteExercise doctorVisit = request.getConcreteExercise();
            ConcreteExerciseEntity oldDoctorVisitEntity = blockingEntityStore
                    .select(ConcreteExerciseEntity.class)
                    .where(ConcreteExerciseEntity.ID.eq(doctorVisit.getId()))
                    .get()
                    .first();

            List<String> imageFilesToDelete = new ArrayList<>();
            if (!TextUtils.isEmpty(oldDoctorVisitEntity.getImageFileName())
                    && !oldDoctorVisitEntity.getImageFileName().equals(doctorVisit.getImageFileName())) {
                imageFilesToDelete.add(oldDoctorVisitEntity.getImageFileName());
            }

            boolean needToAddEvents = ObjectUtils.isFalse(oldDoctorVisitEntity.isExported())
                    && ObjectUtils.isTrue(doctorVisit.getIsExported());

            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            ConcreteExercise result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = updateConcreteExercise(result);

            int count = 0;
            if (needToAddEvents) {
                count = eventsGenerator.generateEvents(result);
            }

            return UpsertConcreteExerciseResponse.builder()
                    .request(request)
                    .addedEventsCount(count)
                    .concreteExercise(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }
}