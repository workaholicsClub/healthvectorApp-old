package ru.android.childdiary.presentation.medical;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
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

    public void addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(medicineTakingInteractor.addMedicineTaking(medicineTaking)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedMedicineTaking -> logger.debug("medicine taking added: " + addedMedicineTaking))
                .subscribe(getViewState()::medicineTakingAdded, this::onUnexpectedError));
    }
}
