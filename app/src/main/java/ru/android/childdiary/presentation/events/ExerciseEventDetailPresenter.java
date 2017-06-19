package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.ExerciseEvent;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;

@InjectViewState
public class ExerciseEventDetailPresenter extends EventDetailPresenter<ExerciseEventDetailView, ExerciseEvent> {
    @Inject
    ExerciseInteractor exerciseInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public Disposable listenForFieldsUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> exerciseEventNameObservable) {
        return calendarInteractor.controlExerciseEventFields(exerciseEventNameObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    protected void handleValidationResult(List<CalendarValidationResult> results) {
        for (CalendarValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                continue;
            }
            switch (result.getFieldType()) {
                case EXERCISE_EVENT_NAME:
                    getViewState().exerciseEventNameValidated(valid);
                    break;
            }
        }
    }

    @Override
    protected EventType getEventType() {
        return EventType.EXERCISE;
    }
}