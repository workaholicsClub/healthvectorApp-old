package ru.android.childdiary.presentation.medical.fragments.visits;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class DoctorVisitsPresenter extends AppPartitionPresenter<DoctorVisitsView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

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

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .firstOrError()
                .toObservable()
                .flatMap(child -> doctorVisitInteractor.getDoctorVisits())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetResult, this::onUnexpectedError));
    }

    private void onGetResult(List<DoctorVisit> medicineTakingList) {
        getViewState().showDoctorVisits(DoctorVisitsFilter.builder().build(), medicineTakingList);
    }
}
