package ru.android.childdiary.presentation.medical.partitions.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListResponse;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.medical.MedicineTakingParameters;
import ru.android.childdiary.presentation.medical.filter.medicines.MedicineTakingFilterDialogArguments;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class MedicineTakingListPresenter extends BasePresenter<MedicineTakingListView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    private Disposable subscription;

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

    private void requestData(@NonNull GetMedicineTakingListRequest request) {
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
                .filter(response.getRequest().getFilter())
                .build());
    }

    public void requestFilterDialog() {
        unsubscribeOnDestroy(
                Observable.combineLatest(medicineTakingInteractor.getMedicines()
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
}
