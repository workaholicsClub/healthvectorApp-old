package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.childdiary.presentation.core.events.BaseAddItemPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class AddConcreteExercisePresenter extends BaseAddItemPresenter<AddConcreteExerciseView, ConcreteExercise> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull ConcreteExercise concreteExercise) {
        showProgressAdd(concreteExercise);
        unsubscribeOnDestroy(
                exerciseInteractor.addConcreteExercise(UpsertConcreteExerciseRequest.builder()
                        .concreteExercise(concreteExercise)
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .doOnNext(added -> hideProgressAdd(concreteExercise))
                        .doOnError(throwable -> hideProgressAdd(concreteExercise))
                        .subscribe(response -> getViewState().added(response.getConcreteExercise(), response.getAddedEventsCount()),
                                this::onUnexpectedError));
    }

    private void showProgressAdd(@NonNull ConcreteExercise concreteExercise) {
        if (ObjectUtils.isTrue(concreteExercise.getIsExported())) {
            getViewState().showGeneratingEvents(true);
        }
    }

    private void hideProgressAdd(@NonNull ConcreteExercise concreteExercise) {
        if (ObjectUtils.isTrue(concreteExercise.getIsExported())) {
            getViewState().showGeneratingEvents(false);
        }
    }

    @Override
    public void handleValidationResult(List<EventValidationResult> results) {
        for (EventValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                continue;
            }
            switch (result.getFieldType()) {
                case CONCRETE_EXERCISE_NAME:
                    getViewState().concreteExerciseNameValidated(valid);
                    break;
            }
        }
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> concreteExerciseNameObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return exerciseInteractor.controlDoneButton(
                concreteExerciseNameObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> concreteExerciseNameObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return exerciseInteractor.controlFields(
                concreteExerciseNameObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }
}