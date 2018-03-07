package ru.android.healthvector.presentation.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.CalendarInteractor;
import ru.android.healthvector.domain.core.validation.EventValidationException;
import ru.android.healthvector.domain.core.validation.EventValidationResult;
import ru.android.healthvector.domain.dictionaries.doctors.DoctorInteractor;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.MedicineMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.exercises.ExerciseInteractor;
import ru.android.healthvector.domain.medical.DoctorVisitInteractor;
import ru.android.healthvector.domain.medical.MedicineTakingInteractor;
import ru.android.healthvector.presentation.core.BasePresenter;
import ru.android.healthvector.utils.ObjectUtils;

public abstract class BaseItemPresenter<V extends BaseItemView<T>, T extends Serializable> extends BasePresenter<V> {
    @Inject
    protected DoctorInteractor doctorInteractor;

    @Inject
    protected DoctorVisitInteractor doctorVisitInteractor;

    @Inject
    protected MedicineInteractor medicineInteractor;

    @Inject
    protected MedicineMeasureInteractor medicineMeasureInteractor;

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

    public void checkValue(@Nullable Medicine medicine) {
        if (medicine == null || medicine.getId() == null) {
            return;
        }
        unsubscribeOnDestroy(medicineInteractor.getAll()
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
        unsubscribeOnDestroy(medicineMeasureInteractor.getAll()
                .first(Collections.emptyList())
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
                logger.error("validation results empty");
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
