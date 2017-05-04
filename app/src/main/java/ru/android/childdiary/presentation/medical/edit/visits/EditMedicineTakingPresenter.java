package ru.android.childdiary.presentation.medical.edit.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.presentation.core.events.BaseEditItemPresenter;

@InjectViewState
public class EditMedicineTakingPresenter extends BaseEditItemPresenter<EditMedicineTakingView, MedicineTaking> {
    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(medicineTakingInteractor.getMedicineMeasureList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(medicineMeasureList -> logger.debug("showMedicineMeasureList: " + medicineMeasureList))
                .subscribe(getViewState()::showMedicineMeasureList, this::onUnexpectedError));
    }

    @Override
    public void update(@NonNull MedicineTaking medicineTaking) {
        // TODO update
        unsubscribeOnDestroy(
                medicineTakingInteractor.addMedicineTaking(medicineTaking)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }
}
