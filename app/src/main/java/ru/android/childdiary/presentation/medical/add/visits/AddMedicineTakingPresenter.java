package ru.android.childdiary.presentation.medical.add.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.childdiary.presentation.medical.core.BaseAddItemPresenter;

@InjectViewState
public class AddMedicineTakingPresenter extends BaseAddItemPresenter<AddMedicineTakingView, MedicineTaking> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull MedicineTaking medicineTaking) {
        getViewState().setLoading(true);
        unsubscribeOnDestroy(
                medicineTakingInteractor.addMedicineTaking(medicineTaking)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .doOnNext(added -> getViewState().setLoading(false))
                        .doOnError(throwable -> getViewState().setLoading(false))
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return medicineTakingInteractor.controlDoneButton(
                medicineObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return medicineTakingInteractor.controlFields(
                medicineObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }
}
