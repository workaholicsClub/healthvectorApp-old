package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ExerciseDetailPresenter extends BasePresenter<ExerciseDetailView> {
    @Inject
    ExerciseInteractor exerciseInteractor;

    private Exercise exercise;

    private Disposable exerciseSubscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void setExercise(@NonNull Exercise exercise) {
        this.exercise = exercise;
        loadExercise(exercise);
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
        this.exercise = exercise;
        getViewState().showExercise(exercise);
    }
}
