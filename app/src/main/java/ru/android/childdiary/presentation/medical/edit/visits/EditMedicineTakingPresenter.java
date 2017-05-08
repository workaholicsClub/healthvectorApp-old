package ru.android.childdiary.presentation.medical.edit.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.events.BaseEditItemPresenter;

@InjectViewState
public class EditMedicineTakingPresenter extends BaseEditItemPresenter<EditMedicineTakingView, MedicineTaking> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void update(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(
                medicineTakingInteractor.updateMedicineTaking(medicineTaking)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }
}
