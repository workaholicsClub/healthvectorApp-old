package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class MedicalDataPresenter extends AppPartitionPresenter<MedicalDataView> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addDoctorVisit() {
        unsubscribeOnDestroy(doctorVisitInteractor.getDefaultDoctorVisit()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(defaultDoctorVisit -> getViewState().navigateToDoctorVisitAdd(defaultDoctorVisit),
                        this::onUnexpectedError));
    }

    public void addMedicineTaking() {
        unsubscribeOnDestroy(medicineTakingInteractor.getDefaultMedicineTaking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(defaultMedicineTaking -> getViewState().navigateToMedicineTakingAdd(defaultMedicineTaking),
                        this::onUnexpectedError));
    }
}
