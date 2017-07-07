package ru.android.childdiary.data.repositories.exercises;

import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import ru.android.childdiary.data.network.dto.Programs;
import ru.android.childdiary.data.repositories.core.mappers.Mapper;
import ru.android.childdiary.data.repositories.exercises.mappers.ProgramsToExercisesMapper;
import ru.android.childdiary.domain.core.TryCountExceededException;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseRepository;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseResponse;
import ru.android.childdiary.utils.strings.StringUtils;

@Singleton
public class ExerciseDataRepository implements ExerciseRepository {
    private static final String KEY_LAST_UPDATE_TIME = "ExerciseDataRepository.last_update_exercises_time";
    private static final int UPDATE_EXERCISES_INTERVAL = 1000 * 60 * 60 * 24; // 1 day

    private static final int MAX_TRY_COUNT = 5;

    private final Logger logger = LoggerFactory.getLogger(toString());

    private final RxSharedPreferences preferences;
    private final ExerciseNetworkService exerciseNetworkService;
    private final Mapper<Programs, List<Exercise>> mapper;
    private final ExerciseDbService exerciseDbService;

    @Inject
    public ExerciseDataRepository(RxSharedPreferences preferences,
                                  ExerciseNetworkService exerciseNetworkService,
                                  ProgramsToExercisesMapper mapper,
                                  ExerciseDbService exerciseDbService) {
        this.preferences = preferences;
        this.exerciseNetworkService = exerciseNetworkService;
        this.mapper = mapper;
        this.exerciseDbService = exerciseDbService;
    }

    private static long nextTryDelayConst(int retryCount) {
        return 3;
    }

    private static long nextTryDelayLinear(int retryCount) {
        return retryCount * 10;
    }

    @Override
    public Observable<Exercise> getExercise(@NonNull Exercise exercise) {
        return exerciseDbService.getExercise(exercise);
    }

    @Override
    public Observable<List<Exercise>> getExercises(@NonNull Child child) {
        return exerciseDbService.getExercises(child);
    }

    @Override
    public Observable<List<Exercise>> updateExercises() {
        logger.debug("force update data");
        return loadDataFromNetwork(MAX_TRY_COUNT, ExerciseDataRepository::nextTryDelayConst)
                .doOnNext(receivedExercises -> logger.debug("received data: " + StringUtils.toString(receivedExercises)))
                .flatMap(exerciseDbService::putExercises)
                .doOnNext(refreshedExercises -> logger.debug("refreshed data: " + StringUtils.toString(refreshedExercises)))
                .flatMap(this::saveUpdateTime)
                .doOnError(throwable -> logger.error("data not received", throwable));
    }

    @Override
    public Observable<List<Exercise>> updateExercisesIfNeeded() {
        return exerciseDbService.getExercises()
                .first(Collections.emptyList())
                .toObservable()
                .flatMap(exercises -> {
                    if (isDataEmpty(exercises) || isDataExpired()) {
                        logger.debug("need to update data");
                        return loadDataFromNetwork(MAX_TRY_COUNT, ExerciseDataRepository::nextTryDelayLinear)
                                .doOnNext(receivedExercises -> logger.debug("received data: " + StringUtils.toString(receivedExercises)))
                                .flatMap(exerciseDbService::putExercises)
                                .doOnNext(refreshedExercises -> logger.debug("refreshed data: " + StringUtils.toString(refreshedExercises)))
                                .flatMap(this::saveUpdateTime)
                                .doOnError(throwable -> logger.error("data not received", throwable));
                    } else {
                        logger.debug("data is actual");
                        return Observable.just(exercises);
                    }
                });
    }

    private Observable<List<Exercise>> saveUpdateTime(@NonNull List<Exercise> exercises) {
        return Observable.fromCallable(() -> {
            preferences.getLong(KEY_LAST_UPDATE_TIME).set(System.currentTimeMillis());
            return exercises;
        });
    }

    private Observable<List<Exercise>> loadDataFromNetwork(int maxTryCount,
                                                           @NonNull Function<? super Integer, ? extends Long> tryToDelayMapper) {
        return exerciseNetworkService.getPrograms()
                .map(mapper::map)
                .retryWhen(errors -> errors
                        .zipWith(Observable.range(1, maxTryCount), (throwable, i) -> {
                            if (i == maxTryCount) {
                                throw new TryCountExceededException("Failed on " + maxTryCount + " try");
                            } else {
                                return i;
                            }
                        })
                        .doOnNext(retryCount -> logger.debug("try " + retryCount))
                        .map(tryToDelayMapper)
                        .doOnNext(delay -> logger.debug("next try will start in " + delay + " seconds"))
                        .flatMap(delay -> Observable.timer(delay, TimeUnit.SECONDS)));
    }

    private boolean isDataEmpty(List<Exercise> exercises) {
        if (exercises.isEmpty()) {
            logger.debug("data is empty");
        }
        return false;
    }

    private boolean isDataExpired() {
        Long value = preferences.getLong(KEY_LAST_UPDATE_TIME).get();
        long lastUpdateTime = value == null ? 0 : value;
        long delta = System.currentTimeMillis() - lastUpdateTime;
        logger.debug("delta is " + delta);
        if (delta > UPDATE_EXERCISES_INTERVAL) {
            logger.debug("data is expired");
            return true;
        }
        return false;
    }

    @Override
    public Single<Boolean> hasConnectedEvents(@NonNull Exercise exercise) {
        return exerciseDbService.hasConnectedEvents(exercise);
    }

    @Override
    public Observable<UpsertConcreteExerciseResponse> addConcreteExercise(@NonNull UpsertConcreteExerciseRequest request) {
        return exerciseDbService.add(request);
    }
}
