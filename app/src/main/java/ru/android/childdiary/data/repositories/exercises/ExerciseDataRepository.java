package ru.android.childdiary.data.repositories.exercises;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.data.dto.Programs;
import ru.android.childdiary.data.repositories.core.mappers.Mapper;
import ru.android.childdiary.data.repositories.exercises.mappers.ProgramsToExercisesMapper;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseRepository;

@Singleton
public class ExerciseDataRepository implements ExerciseRepository {
    private static final String KEY_LAST_UPDATE_TIME = "ExerciseDataRepository.last_update_exercises_time";
    private static final int UPDATE_EXERCISES_INTERVAL = 1000 * 60 * 60 * 24; // 1 day

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

    @Override
    public Observable<List<Exercise>> getExercises() {
        return exerciseDbService.getExercises();
    }

    @Override
    public Observable<List<Exercise>> updateExercisesIfNeeded() {
        return exerciseDbService.getExercises()
                .flatMap(exercises -> {
                    if (isDataEmpty(exercises) || isDataExpired()) {
                        logger.debug("need to update data");
                        return loadDataFromNetwork()
                                .flatMap(exerciseDbService::putExercises);
                    } else {
                        logger.debug("data is actual");
                        return Observable.just(exercises);
                    }
                });
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

    private Observable<List<Exercise>> loadDataFromNetwork() {
        return exerciseNetworkService.getPrograms()
                .map(mapper::map)
                .retryWhen(errors -> errors
                        .zipWith(Observable.range(1, 5), (throwable, i) -> i)
                        .flatMap(retryCount -> Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS)));
    }
}
