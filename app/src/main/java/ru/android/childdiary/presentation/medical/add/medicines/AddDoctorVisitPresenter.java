package ru.android.childdiary.presentation.medical.add.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.events.BaseAddItemPresenter;

@InjectViewState
public class AddDoctorVisitPresenter extends BaseAddItemPresenter<AddDoctorVisitView, DoctorVisit> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull DoctorVisit doctorVisit) {
        getViewState().setButtonAddEnabled(false);
        unsubscribeOnDestroy(
                doctorVisitInteractor.addDoctorVisit(doctorVisit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .doOnNext(added -> getViewState().setButtonAddEnabled(true))
                        .doOnError(throwable -> getViewState().setButtonAddEnabled(true))
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }
}
