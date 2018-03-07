package ru.android.healthvector.presentation.medical;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.core.requests.ChildResponse;
import ru.android.healthvector.domain.medical.DoctorVisitInteractor;
import ru.android.healthvector.domain.medical.MedicineTakingInteractor;
import ru.android.healthvector.presentation.core.AppPartitionPresenter;

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
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> Observable.combineLatest(
                                doctorVisitInteractor.getDefaultDoctorVisit(),
                                doctorVisitInteractor.getStartTimeOnce(),
                                doctorVisitInteractor.getFinishTimeOnce(),
                                (defaultDoctorVisit, startTime, finishTime) -> DoctorVisitParameters.builder()
                                        .defaultDoctorVisit(defaultDoctorVisit)
                                        .startTime(startTime)
                                        .finishTime(finishTime)
                                        .build())
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    DoctorVisitParameters parameters = response.getResponse();
                                    getViewState().navigateToDoctorVisitAdd(
                                            parameters.getDefaultDoctorVisit(),
                                            parameters.getStartTime(),
                                            parameters.getFinishTime());
                                },
                                this::onUnexpectedError));
    }

    public void addMedicineTaking() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> Observable.combineLatest(
                                medicineTakingInteractor.getDefaultMedicineTaking(),
                                medicineTakingInteractor.getStartTimeOnce(),
                                medicineTakingInteractor.getFinishTimeOnce(),
                                (defaultMedicineTaking, startTime, finishTime) -> MedicineTakingParameters.builder()
                                        .defaultMedicineTaking(defaultMedicineTaking)
                                        .startTime(startTime)
                                        .finishTime(finishTime)
                                        .build())
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    MedicineTakingParameters parameters = response.getResponse();
                                    getViewState().navigateToMedicineTakingAdd(
                                            parameters.getDefaultMedicineTaking(),
                                            parameters.getStartTime(),
                                            parameters.getFinishTime());
                                },
                                this::onUnexpectedError));
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
