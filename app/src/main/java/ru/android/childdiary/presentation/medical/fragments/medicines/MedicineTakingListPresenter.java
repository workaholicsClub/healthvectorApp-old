package ru.android.childdiary.presentation.medical.fragments.medicines;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class MedicineTakingListPresenter extends AppPartitionPresenter<MedicineTakingListView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void requestMedicineDetail() {
        getViewState().navigateToMedicineTaking();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .firstOrError()
                .toObservable()
                .flatMap(child -> medicineTakingInteractor.getMedicineTakingList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetResult, this::onUnexpectedError));
    }

    private void onGetResult(List<MedicineTaking> medicineTakingList) {
        getViewState().showMedicineTakingList(MedicineTakingListFilter.builder().build(), medicineTakingList);
    }
}
