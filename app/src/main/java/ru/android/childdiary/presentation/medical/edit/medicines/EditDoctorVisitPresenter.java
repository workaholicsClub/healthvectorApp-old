package ru.android.childdiary.presentation.medical.edit.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.presentation.core.events.BaseEditItemPresenter;

@InjectViewState
public class EditDoctorVisitPresenter extends BaseEditItemPresenter<EditDoctorVisitView, DoctorVisit> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void update(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(
                doctorVisitInteractor.updateDoctorVisit(doctorVisit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }
}
