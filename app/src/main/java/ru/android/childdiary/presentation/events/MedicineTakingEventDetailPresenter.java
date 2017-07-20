package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class MedicineTakingEventDetailPresenter extends EventDetailPresenter<MedicineTakingEventDetailView, MedicineTakingEvent> {
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
        unsubscribeOnDestroy(medicineTakingInteractor.getMedicineMeasureList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showMedicineMeasureValueDialog, this::onUnexpectedError));
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
}
