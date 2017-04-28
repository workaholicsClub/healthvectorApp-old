package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class AddMedicineTakingPresenter extends BasePresenter<AddMedicineTakingView> {
    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(medicineTakingInteractor.getMedicines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(medicines -> logger.debug("showMedicines: " + medicines))
                .subscribe(getViewState()::showMedicines, this::onUnexpectedError));
    }
}
