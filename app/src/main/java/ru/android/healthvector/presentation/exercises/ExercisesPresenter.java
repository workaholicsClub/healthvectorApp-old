package ru.android.healthvector.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.exceptions.TryCountExceededException;
import ru.android.healthvector.domain.exercises.ExerciseInteractor;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.presentation.core.AppPartitionPresenter;

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

    public void addConcreteExercise(@NonNull Exercise exercise) {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> Observable.combineLatest(
                                exerciseInteractor.getDefaultConcreteExercise(child, exercise),
                                exerciseInteractor.getStartTimeOnce(),
                                exerciseInteractor.getFinishTimeOnce(),
                                (defaultDoctorVisit, startTime, finishTime) -> ConcreteExerciseParameters.builder()
                                        .defaultConcreteExercise(defaultDoctorVisit)
                                        .startTime(startTime)
                                        .finishTime(finishTime)
                                        .build()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(parameters -> {
                                    if (parameters.getDefaultConcreteExercise().getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                    } else {
                                        getViewState().navigateToConcreteExerciseAdd(
                                                parameters.getDefaultConcreteExercise(),
                                                parameters.getStartTime(),
                                                parameters.getFinishTime());
                                    }
                                },
                                this::onUnexpectedError));
    }

    public void tryToLoadDataFromNetwork() {
        getViewState().startLoading();
        unsubscribeOnDestroy(
                exerciseInteractor.updateExercises()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                exercises -> {
                                    getViewState().stopLoading();
                                    updateData();
                                },
                                throwable -> {
                                    getViewState().stopLoading();
                                    updateData();
                                    if (throwable instanceof TryCountExceededException) {
                                        return;
                                    }
                                    onUnexpectedError(throwable);
                                })
        );
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
