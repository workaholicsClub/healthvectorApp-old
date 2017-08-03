package ru.android.childdiary.presentation.medical.add.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.interactors.calendar.data.core.LinearGroups;
import ru.android.childdiary.domain.interactors.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitRequest;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.childdiary.presentation.core.events.BaseAddItemPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class AddDoctorVisitPresenter extends BaseAddItemPresenter<AddDoctorVisitView, DoctorVisit> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull DoctorVisit doctorVisitToAdd) {
        DoctorVisit doctorVisit = preprocess(doctorVisitToAdd);
        showProgressAdd(doctorVisit);
        unsubscribeOnDestroy(
                doctorVisitInteractor.addDoctorVisit(UpsertDoctorVisitRequest.builder()
                        .doctorVisit(doctorVisit)
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .doOnNext(added -> hideProgressAdd(doctorVisit))
                        .doOnError(throwable -> hideProgressAdd(doctorVisit))
                        .subscribe(response -> getViewState().added(response.getDoctorVisit(), response.getAddedEventsCount()),
                                this::onUnexpectedError));
    }

    private DoctorVisit preprocess(@NonNull DoctorVisit doctorVisit) {
        RepeatParameters repeatParameters = doctorVisit.getRepeatParameters();
        if (repeatParameters == null) {
            return doctorVisit;
        }
        LinearGroups linearGroups = repeatParameters.getFrequency();
        if (linearGroups == null || linearGroups.getTimes().size() != 1) {
            return doctorVisit;
        }
        // Особым образом обрабатываем ситуацию, когда количество повторений в день равно 1:
        // подменяем время в нулевой линейной группе
        linearGroups = linearGroups.withTime(0, doctorVisit.getDateTime().toLocalTime());
        return doctorVisit.toBuilder()
                .repeatParameters(repeatParameters.toBuilder()
                        .frequency(linearGroups)
                        .build())
                .build();
    }

    private void showProgressAdd(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            getViewState().showGeneratingEvents(true);
        }
    }

    private void hideProgressAdd(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
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
                case DOCTOR_VISIT_NAME:
                    getViewState().doctorVisitNameValidated(valid);
                    break;
            }
        }
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitNameObservable,
            @NonNull FieldValueChangeEventsObservable<Doctor> doctorObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return doctorVisitInteractor.controlDoneButton(
                doctorVisitNameObservable,
                doctorObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitNameObservable,
            @NonNull FieldValueChangeEventsObservable<Doctor> doctorObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return doctorVisitInteractor.controlFields(
                doctorVisitNameObservable,
                doctorObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    protected EventType getEventType() {
        return EventType.DOCTOR_VISIT;
    }
}
