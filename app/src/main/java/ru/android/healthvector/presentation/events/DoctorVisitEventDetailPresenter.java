package ru.android.healthvector.presentation.events;

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
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.validation.CalendarValidationResult;
import ru.android.healthvector.domain.dictionaries.doctors.DoctorInteractor;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.medical.DoctorVisitInteractor;
import ru.android.healthvector.presentation.events.core.PeriodicEventDetailPresenter;
import ru.android.healthvector.utils.ObjectUtils;

@InjectViewState
public class DoctorVisitEventDetailPresenter extends PeriodicEventDetailPresenter<DoctorVisitEventDetailView, DoctorVisitEvent> {
    @Inject
    DoctorInteractor doctorInteractor;

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
        unsubscribeOnDestroy(doctorInteractor.getAll()
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

    @Override
    protected Observable<Integer> generateEvents(@NonNull DoctorVisitEvent event, @NonNull LengthValue lengthValue) {
        return doctorVisitInteractor.continueLinearGroup(
                event.getDoctorVisit(),
                event.getDateTime().toLocalDate(),
                event.getLinearGroup(),
                lengthValue
        );
    }
}
