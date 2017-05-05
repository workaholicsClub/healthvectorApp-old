package ru.android.childdiary.presentation.medical.fragments.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListResponse;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class MedicineTakingListPresenter extends AppPartitionPresenter<MedicineTakingListView> {
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
                Observable.just(MedicineTakingListRequest.builder().build()),
                childInteractor.getActiveChild(),
                (request, child) -> request.toBuilder().child(child).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(MedicineTakingListRequest request) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(medicineTakingInteractor.getMedicineTakingList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull MedicineTakingListResponse response) {
        logger.debug("onGetData: " + response);
        getViewState().showMedicineTakingList(MedicineTakingListFilter.builder().build(), response.getMedicineTakingList());
    }

    public void editMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(medicineTakingInteractor.getDefaultMedicineTaking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(defaultMedicineTaking -> getViewState().navigateToMedicineTaking(medicineTaking, defaultMedicineTaking),
                        this::onUnexpectedError));
    }
}
