package ru.android.healthvector.presentation.medical.partitions.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.ChildInteractor;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.requests.ChildResponse;
import ru.android.healthvector.domain.core.requests.HasDataResponse;
import ru.android.healthvector.domain.dictionaries.doctors.DoctorInteractor;
import ru.android.healthvector.domain.medical.DoctorVisitInteractor;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsRequest;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsResponse;
import ru.android.healthvector.presentation.core.adapters.DeletedItemsManager;
import ru.android.healthvector.presentation.medical.DoctorVisitParameters;
import ru.android.healthvector.presentation.medical.filter.adapters.Chips;
import ru.android.healthvector.presentation.medical.filter.adapters.ChipsUtils;
import ru.android.healthvector.presentation.medical.filter.visits.DoctorVisitFilterDialogArguments;
import ru.android.healthvector.presentation.medical.partitions.core.BaseMedicalDataPresenter;
import ru.android.healthvector.utils.ObjectUtils;

@InjectViewState
public class DoctorVisitsPresenter extends BaseMedicalDataPresenter<DoctorVisitsView> {
    private final DeletedItemsManager<DoctorVisit> deletedItemsManager = new DeletedItemsManager<>();

    @Inject
    ChildInteractor childInteractor;

    @Inject
    DoctorInteractor doctorInteractor;

    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    private Disposable subscription;
    private GetDoctorVisitsRequest request;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.combineLatest(
                doctorVisitInteractor.getSelectedFilterValue(),
                childInteractor.getActiveChild(),
                (filter, child) -> GetDoctorVisitsRequest.builder()
                        .child(child)
                        .filter(filter)
                        .build())
                .doOnNext(request -> logger.debug("onGetRequest: " + request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::requestData, this::onUnexpectedError));
    }

    public void updateData() {
        if (request == null) {
            logger.error("request is null");
            return;
        }
        requestData(request);
    }

    private void requestData(@NonNull GetDoctorVisitsRequest request) {
        this.request = request;
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(doctorVisitInteractor.getDoctorVisits(request)
                .doOnNext(response -> logger.debug("onGetData: " + response))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull GetDoctorVisitsResponse response) {
        getViewState().showDoctorVisitsState(DoctorVisitsState.builder()
                .child(response.getRequest().getChild())
                .doctorVisits(response.getDoctorVisits())
                .chips(ChipsUtils.mapFilterToChips(response.getRequest().getFilter()))
                .build());
    }

    @Override
    protected Single<HasDataResponse> hasDataToFilter(@NonNull Child child) {
        return doctorVisitInteractor.hasDataToFilter(child);
    }

    @Override
    protected void showFilterDialog() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        doctorInteractor.getAll()
                                .first(Collections.emptyList())
                                .toObservable(),
                        doctorVisitInteractor.getSelectedFilterValueOnce(),
                        ((doctors, filter) -> DoctorVisitFilterDialogArguments.builder()
                                .items(doctors)
                                .selectedItems(filter.getSelectedItems())
                                .fromDate(filter.getFromDate())
                                .toDate(filter.getToDate())
                                .build()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showFilterDialog, this::onUnexpectedError));
    }

    public void editDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        if (deletedItemsManager.check(doctorVisit)) {
            return;
        }
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
        if (deletedItemsManager.check(doctorVisit)) {
            return;
        }
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            unsubscribeOnDestroy(doctorVisitInteractor.hasConnectedEvents(doctorVisit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(hasConnectedEvents -> {
                        if (hasConnectedEvents) {
                            getViewState().askDeleteConnectedEventsOrNot(doctorVisit);
                        } else {
                            getViewState().confirmDeleteDoctorVisit(doctorVisit);
                        }
                    }, this::onUnexpectedError));
        } else {
            getViewState().confirmDeleteDoctorVisit(doctorVisit);
        }
    }

    public void deleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        if (deletedItemsManager.checkAndAdd(doctorVisit)) {
            return;
        }
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

    @Override
    public void setFilter(@NonNull List<Chips> chips) {
        unsubscribeOnDestroy(doctorVisitInteractor.setSelectedFilterValueObservable(ChipsUtils.mapToDoctorFilter(chips))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(filter -> {
                }, this::onUnexpectedError));
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
}
