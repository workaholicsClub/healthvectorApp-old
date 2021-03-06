package ru.android.healthvector.presentation.development.partitions.antropometry.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.healthvector.presentation.development.partitions.antropometry.core.AntropometryPresenter;

@InjectViewState
public class AddAntropometryPresenter extends AntropometryPresenter<AddAntropometryView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void add(@NonNull Antropometry antropometry) {
        unsubscribeOnDestroy(
                antropometryInteractor.add(antropometry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull FieldValueChangeEventsObservable<Double> heightObservable,
            @NonNull FieldValueChangeEventsObservable<Double> weightObservable) {
        return antropometryInteractor.controlDoneButton(
                heightObservable,
                weightObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }
}
