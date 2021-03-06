package ru.android.healthvector.presentation.medical.partitions.medicines;

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
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.requests.ChildResponse;
import ru.android.healthvector.domain.core.requests.HasDataResponse;
import ru.android.healthvector.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.healthvector.domain.medical.MedicineTakingInteractor;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListRequest;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListResponse;
import ru.android.healthvector.presentation.core.adapters.DeletedItemsManager;
import ru.android.healthvector.presentation.medical.MedicineTakingParameters;
import ru.android.healthvector.presentation.medical.filter.adapters.Chips;
import ru.android.healthvector.presentation.medical.filter.adapters.ChipsUtils;
import ru.android.healthvector.presentation.medical.filter.medicines.MedicineTakingFilterDialogArguments;
import ru.android.healthvector.presentation.medical.partitions.core.BaseMedicalDataPresenter;
import ru.android.healthvector.utils.ObjectUtils;

@InjectViewState
public class MedicineTakingListPresenter extends BaseMedicalDataPresenter<MedicineTakingListView> {
    private final DeletedItemsManager<MedicineTaking> deletedItemsManager = new DeletedItemsManager<>();

    @Inject
    MedicineInteractor medicineInteractor;

    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    private Disposable subscription;
    private GetMedicineTakingListRequest request;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.combineLatest(
                medicineTakingInteractor.getSelectedFilterValue(),
                childInteractor.getActiveChild(),
                (filter, child) -> GetMedicineTakingListRequest.builder()
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

    private void requestData(@NonNull GetMedicineTakingListRequest request) {
        this.request = request;
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(medicineTakingInteractor.getMedicineTakingList(request)
                .doOnNext(response -> logger.debug("onGetData: " + response))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull GetMedicineTakingListResponse response) {
        getViewState().showMedicineTakingListState(MedicineTakingListState.builder()
                .child(response.getRequest().getChild())
                .medicineTakingList(response.getMedicineTakingList())
                .chips(ChipsUtils.mapFilterToChips(response.getRequest().getFilter()))
                .build());
    }

    @Override
    protected Single<HasDataResponse> hasDataToFilter(@NonNull Child child) {
        return medicineTakingInteractor.hasDataToFilter(child);
    }

    @Override
    protected void showFilterDialog() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        medicineInteractor.getAll()
                                .first(Collections.emptyList())
                                .toObservable(),
                        medicineTakingInteractor.getSelectedFilterValueOnce(),
                        ((medicines, filter) -> MedicineTakingFilterDialogArguments.builder()
                                .items(medicines)
                                .selectedItems(filter.getSelectedItems())
                                .fromDate(filter.getFromDate())
                                .toDate(filter.getToDate())
                                .build()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showFilterDialog, this::onUnexpectedError));
    }

    public void editMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        if (deletedItemsManager.check(medicineTaking)) {
            return;
        }
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        medicineTakingInteractor.getDefaultMedicineTaking(),
                        medicineTakingInteractor.getStartTimeOnce(),
                        medicineTakingInteractor.getFinishTimeOnce(),
                        (defaultMedicineTaking, startTime, finishTime) -> MedicineTakingParameters.builder()
                                .defaultMedicineTaking(defaultMedicineTaking)
                                .startTime(startTime)
                                .finishTime(finishTime)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(parameters -> getViewState().navigateToMedicineTaking(
                                medicineTaking,
                                parameters.getDefaultMedicineTaking(),
                                parameters.getStartTime(),
                                parameters.getFinishTime()),
                                this::onUnexpectedError));
    }

    public void delete(@NonNull MedicineTaking medicineTaking) {
        if (deletedItemsManager.check(medicineTaking)) {
            return;
        }
        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            unsubscribeOnDestroy(medicineTakingInteractor.hasConnectedEvents(medicineTaking)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(hasConnectedEvents -> {
                        if (hasConnectedEvents) {
                            getViewState().askDeleteConnectedEventsOrNot(medicineTaking);
                        } else {
                            getViewState().confirmDeleteMedicineTaking(medicineTaking);
                        }
                    }, this::onUnexpectedError));
        } else {
            getViewState().confirmDeleteMedicineTaking(medicineTaking);
        }
    }

    public void deleteMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        if (deletedItemsManager.checkAndAdd(medicineTaking)) {
            return;
        }
        unsubscribeOnDestroy(medicineTakingInteractor.deleteMedicineTaking(DeleteMedicineTakingRequest.builder()
                .medicineTaking(medicineTaking)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> getViewState().medicineTakingDeleted(medicineTaking), this::onUnexpectedError));
    }

    public void deleteMedicineTakingWithConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(medicineTakingInteractor.deleteMedicineTakingWithEvents(DeleteMedicineTakingEventsRequest.builder()
                .medicineTaking(medicineTaking)
                .linearGroup(null)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(response -> getViewState().medicineTakingDeleted(medicineTaking), this::onUnexpectedError));
    }

    @Override
    public void setFilter(@NonNull List<Chips> chips) {
        unsubscribeOnDestroy(medicineTakingInteractor.setSelectedFilterValueObservable(ChipsUtils.mapToMedicineFilter(chips))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(filter -> {
                }, this::onUnexpectedError));
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
}
