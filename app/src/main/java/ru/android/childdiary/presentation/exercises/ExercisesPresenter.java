package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class ExercisesPresenter extends AppPartitionPresenter<ExercisesView> {
    @Inject
    ExerciseInteractor exerciseInteractor;

    private Disposable exercisesSubscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        updateData();
    }

    @Override
    protected void onChildLoaded(@NonNull Child child) {
        super.onChildLoaded(child);
        updateData(child);
    }

    public void updateData() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateData, this::onUnexpectedError));
    }

    private void updateData(@NonNull Child child) {
        unsubscribe(exercisesSubscription);
        exercisesSubscription = unsubscribeOnDestroy(
                exerciseInteractor.getExercises(child)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showExercises, this::onUnexpectedError));
    }

    public void showExerciseDetails(@NonNull Exercise exercise) {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .map(child -> ExerciseDetailState.builder()
                                .child(child)
                                .exercise(exercise)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToExercise, this::onUnexpectedError));
    }
}
