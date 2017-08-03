package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.exercises.data.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ExerciseDetailPresenter extends BasePresenter<ExerciseDetailView> {
    @Inject
    ExerciseInteractor exerciseInteractor;

    private Disposable exerciseSubscription;
    private ExerciseDetailState state;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void setExerciseDetailState(@NonNull ExerciseDetailState state) {
        this.state = state;
        loadExercise(state.getExercise());
    }

    private void loadExercise(@NonNull Exercise exercise) {
        unsubscribe(exerciseSubscription);
        exerciseSubscription = unsubscribeOnDestroy(createExerciseLoader(exercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onExerciseLoaded, this::onUnexpectedError));
    }

    private Observable<Exercise> createExerciseLoader(@NonNull Exercise exercise) {
        return exerciseInteractor.getExercise(exercise);
    }

    private void onExerciseLoaded(@NonNull Exercise exercise) {
        this.state = state.toBuilder().exercise(exercise).build();
        getViewState().showExercise(state);
    }

    public void addConcreteExercise() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        exerciseInteractor.getDefaultConcreteExercise(state.getChild(), state.getExercise()),
                        exerciseInteractor.getStartTimeOnce(),
                        exerciseInteractor.getFinishTimeOnce(),
                        (defaultDoctorVisit, startTime, finishTime) -> ConcreteExerciseParameters.builder()
                                .defaultConcreteExercise(defaultDoctorVisit)
                                .startTime(startTime)
                                .finishTime(finishTime)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(parameters -> getViewState().navigateToConcreteExerciseAdd(
                                parameters.getDefaultConcreteExercise(),
                                parameters.getStartTime(),
                                parameters.getFinishTime()),
                                this::onUnexpectedError));
    }

    public void openUrl(@NonNull String url) {
        String title = state.getExercise().getName();
        getViewState().navigateToWebBrowser(title, url);
    }
}
