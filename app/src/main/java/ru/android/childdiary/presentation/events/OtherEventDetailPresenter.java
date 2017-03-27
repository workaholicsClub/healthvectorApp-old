package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;

@InjectViewState
public class OtherEventDetailPresenter extends EventDetailPresenter<OtherEventDetailView, OtherEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public Disposable listenForFieldsUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> otherEventNameObservable) {
        return calendarInteractor.controlFields(otherEventNameObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    protected void handleValidationResult(List<CalendarValidationResult> results) {
        for (CalendarValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                return;
            }
            switch (result.getFieldType()) {
                case OTHER_EVENT_NAME:
                    getViewState().otherEventNameValidated(valid);
                    break;
            }
        }
    }
}
