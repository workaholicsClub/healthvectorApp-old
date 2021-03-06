package ru.android.healthvector.presentation.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.MedicineMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.MedicineTakingInteractor;
import ru.android.healthvector.presentation.events.core.PeriodicEventDetailPresenter;
import ru.android.healthvector.utils.ObjectUtils;

@InjectViewState
public class MedicineTakingEventDetailPresenter extends PeriodicEventDetailPresenter<MedicineTakingEventDetailView, MedicineTakingEvent> {
    @Inject
    MedicineInteractor medicineInteractor;

    @Inject
    MedicineMeasureInteractor medicineMeasureInteractor;

    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected EventType getEventType() {
        return EventType.MEDICINE_TAKING;
    }

    public void requestMedicineMeasureValueDialog() {
        unsubscribeOnDestroy(medicineMeasureInteractor.getAll()
                .first(Collections.emptyList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showMedicineMeasureValueDialog, this::onUnexpectedError));
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

    @Override
    protected Observable<Integer> generateEvents(@NonNull MedicineTakingEvent event, @NonNull LengthValue lengthValue) {
        return medicineTakingInteractor.continueLinearGroup(
                event.getMedicineTaking(),
                event.getDateTime().toLocalDate(),
                event.getLinearGroup(),
                lengthValue
        );
    }
}
