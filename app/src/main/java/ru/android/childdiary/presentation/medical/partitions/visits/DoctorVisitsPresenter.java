package ru.android.childdiary.presentation.medical.partitions.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsResponse;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;
import ru.android.childdiary.presentation.medical.DoctorVisitParameters;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class DoctorVisitsPresenter extends AppPartitionPresenter<DoctorVisitsView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    private Disposable subscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.combineLatest(
                Observable.just(GetDoctorVisitsRequest.builder().build()),
                childInteractor.getActiveChild(),
                (request, child) -> request.toBuilder().child(child).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(GetDoctorVisitsRequest request) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(doctorVisitInteractor.getDoctorVisits(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull GetDoctorVisitsResponse response) {
        logger.debug("onGetData: " + response);
        getViewState().showDoctorVisits(DoctorVisitsFilter.builder().build(), response.getDoctorVisits());
    }

    public void editDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        doctorVisitInteractor.getDefaultDoctorVisit(),
                        doctorVisitInteractor.getStartTimeOnce(),
                        doctorVisitInteractor.getFinishTimeOnce(),
                        (defaultDoctorVisit, startTime, finishTime) -> DoctorVisitParameters.builder()
                                .defaultDoctorVisit(defaultDoctorVisit)
                                .startTime(startTime)
                                .finishTime(finishTime)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(parameters -> getViewState().navigateToDoctorVisit(
                                doctorVisit,
                                parameters.getDefaultDoctorVisit(),
                                parameters.getStartTime(),
                                parameters.getFinishTime()),
                                this::onUnexpectedError));
    }

    public void delete(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            getViewState().askDeleteConnectedEventsOrNot(doctorVisit);
        } else {
            deleteDoctorVisit(doctorVisit);
        }
    }

    public void deleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(doctorVisitInteractor.deleteDoctorVisit(DeleteDoctorVisitRequest.builder()
                .doctorVisit(doctorVisit)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> getViewState().doctorVisitDeleted(doctorVisit), this::onUnexpectedError));
    }

    public void deleteDoctorVisitWithConnectedEvents(@NonNull DoctorVisit doctorVisit) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(doctorVisitInteractor.deleteDoctorVisitWithEvents(DeleteDoctorVisitEventsRequest.builder()
                .doctorVisit(doctorVisit)
                .linearGroup(null)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(response -> getViewState().doctorVisitDeleted(doctorVisit), this::onUnexpectedError));
    }
}
