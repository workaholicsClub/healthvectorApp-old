package ru.android.healthvector.presentation.medical.add.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.LinearGroups;
import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.requests.UpsertMedicineTakingRequest;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.healthvector.presentation.core.events.BaseAddItemPresenter;
import ru.android.healthvector.utils.ObjectUtils;

@InjectViewState
public class AddMedicineTakingPresenter extends BaseAddItemPresenter<AddMedicineTakingView, MedicineTaking> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull MedicineTaking medicineTakingToAdd) {
        MedicineTaking medicineTaking = preprocess(medicineTakingToAdd);
        showProgressAdd(medicineTaking);
        unsubscribeOnDestroy(
                medicineTakingInteractor.addMedicineTaking(UpsertMedicineTakingRequest.builder()
                        .medicineTaking(medicineTaking)
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .doOnNext(added -> hideProgressAdd(medicineTaking))
                        .doOnError(throwable -> hideProgressAdd(medicineTaking))
                        .subscribe(response -> getViewState().added(response.getMedicineTaking(), response.getAddedEventsCount()),
                                this::onUnexpectedError));
    }

    private MedicineTaking preprocess(@NonNull MedicineTaking medicineTaking) {
        RepeatParameters repeatParameters = medicineTaking.getRepeatParameters();
        if (repeatParameters == null) {
            return medicineTaking;
        }
        LinearGroups linearGroups = repeatParameters.getFrequency();
        if (linearGroups == null || linearGroups.getTimes().size() != 1) {
            return medicineTaking;
        }
        // Особым образом обрабатываем ситуацию, когда количество повторений в день равно 1:
        // подменяем время в нулевой линейной группе
        linearGroups = linearGroups.withTime(0, medicineTaking.getDateTime().toLocalTime());
        return medicineTaking.toBuilder()
                .repeatParameters(repeatParameters.toBuilder()
                        .frequency(linearGroups)
                        .build())
                .build();
    }

    private void showProgressAdd(@NonNull MedicineTaking medicineTaking) {
        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            getViewState().showGeneratingEvents(true);
        }
    }

    private void hideProgressAdd(@NonNull MedicineTaking medicineTaking) {
        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            getViewState().showGeneratingEvents(false);
        }
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return medicineTakingInteractor.controlDoneButton(
                medicineObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return medicineTakingInteractor.controlFields(
                medicineObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    protected EventType getEventType() {
        return EventType.MEDICINE_TAKING;
    }
}
