package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationException;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.Doctor;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.ObjectUtils;

public abstract class BaseItemPresenter<V extends BaseItemView<T>, T extends Serializable> extends BasePresenter<V> {
    @Inject
    protected DoctorVisitInteractor doctorVisitInteractor;

    @Inject
    protected MedicineTakingInteractor medicineTakingInteractor;

    @Inject
    protected ExerciseInteractor exerciseInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(calendarInteractor.getFrequencyList(getEventType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showFrequencyList));
        unsubscribeOnDestroy(calendarInteractor.getPeriodicityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showPeriodicityList));
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

    public void checkValue(@Nullable Medicine medicine) {
        if (medicine == null || medicine.getId() == null) {
            return;
        }
        unsubscribeOnDestroy(medicineTakingInteractor.getMedicines()
                .first(Collections.emptyList())
                .map(medicines -> findMedicine(medicine, medicines))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setMedicine, this::onUnexpectedError));
    }

    private Medicine findMedicine(@NonNull Medicine medicine, @NonNull List<Medicine> medicines) {
        return Observable
                .fromIterable(medicines)
                .filter(item -> ObjectUtils.equals(medicine.getId(), item.getId()))
                .first(Medicine.NULL)
                .blockingGet();
    }

    public void requestMedicineMeasureValueDialog() {
        unsubscribeOnDestroy(medicineTakingInteractor.getMedicineMeasureList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showMedicineMeasureValueDialog, this::onUnexpectedError));
    }

    public void requestLengthValueDialog() {
        unsubscribeOnDestroy(calendarInteractor.getTimeUnitValues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showLengthValueDialog, this::onUnexpectedError));
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof EventValidationException) {
            List<EventValidationResult> results = ((EventValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("medical validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(EventValidationResult::notValid)
                    .map(EventValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    public void handleValidationResult(List<EventValidationResult> results) {
    }

    protected abstract EventType getEventType();
}
