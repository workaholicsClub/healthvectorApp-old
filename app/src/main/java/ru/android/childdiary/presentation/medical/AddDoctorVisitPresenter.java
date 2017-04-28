package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class AddDoctorVisitPresenter extends BasePresenter<AddDoctorVisitView> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(doctorVisitInteractor.getDoctors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(doctors -> logger.debug("showDoctors: " + doctors))
                .subscribe(getViewState()::showDoctors, this::onUnexpectedError));
    }
}
