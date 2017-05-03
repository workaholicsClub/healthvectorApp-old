package ru.android.childdiary.presentation.medical.fragments.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsResponse;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

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

    public void requestDoctorVisitDetail() {
        getViewState().navigateToDoctorVisit();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.combineLatest(
                Observable.just(DoctorVisitsRequest.builder().build()),
                childInteractor.getActiveChild(),
                (request, child) -> request.toBuilder().child(child).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(DoctorVisitsRequest request) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(doctorVisitInteractor.getDoctorVisits(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull DoctorVisitsResponse response) {
        logger.debug("onGetData: " + response);
        getViewState().showDoctorVisits(DoctorVisitsFilter.builder().build(), response.getDoctorVisits());
    }
}
