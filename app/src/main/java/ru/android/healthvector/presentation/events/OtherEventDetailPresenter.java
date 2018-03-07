package ru.android.healthvector.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.domain.calendar.validation.CalendarValidationResult;
import ru.android.healthvector.presentation.events.core.EventDetailPresenter;

@InjectViewState
public class OtherEventDetailPresenter extends EventDetailPresenter<OtherEventDetailView, OtherEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public Disposable listenForDoneButtonUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return calendarInteractor.controlOtherEventDoneButton(otherEventNameObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return calendarInteractor.controlOtherEventFields(otherEventNameObservable)
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
                case OTHER_EVENT_NAME:
                    getViewState().otherEventNameValidated(valid);
                    break;
            }
        }
    }

    @Override
    protected EventType getEventType() {
        return EventType.OTHER;
    }
}
