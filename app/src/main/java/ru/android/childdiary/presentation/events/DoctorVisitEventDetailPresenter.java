package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.Doctor;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class DoctorVisitEventDetailPresenter extends EventDetailPresenter<DoctorVisitEventDetailView, DoctorVisitEvent> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public Disposable listenForFieldsUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitEventNameObservable) {
        return calendarInteractor.controlDoctorVisitEventFields(doctorVisitEventNameObservable)
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
                case DOCTOR_VISIT_EVENT_NAME:
                    getViewState().doctorVisitEventNameValidated(valid);
                    break;
            }
        }
    }

    @Override
    protected EventType getEventType() {
        return EventType.DOCTOR_VISIT;
    }

    public void checkValue(@Nullable Doctor doctor) {
        if (doctor == null || doctor.getId() == null) {
            return;
        }
        unsubscribeOnDestroy(doctorVisitInteractor.getDoctors()
                .first(Collections.emptyList())
                .map(doctors -> findDoctor(doctor, doctors))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setDoctor, this::onUnexpectedError));
    }

    private Doctor findDoctor(@NonNull Doctor doctor, @NonNull List<Doctor> doctors) {
        return Observable
                .fromIterable(doctors)
                .filter(item -> ObjectUtils.equals(doctor.getId(), item.getId()))
                .first(Doctor.NULL)
                .blockingGet();
    }
}
