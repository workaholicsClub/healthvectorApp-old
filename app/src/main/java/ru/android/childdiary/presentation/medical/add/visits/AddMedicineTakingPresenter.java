package ru.android.childdiary.presentation.medical.add.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.events.BaseAddItemPresenter;

@InjectViewState
public class AddMedicineTakingPresenter extends BaseAddItemPresenter<AddMedicineTakingView, MedicineTaking> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(
                medicineTakingInteractor.addMedicineTaking(medicineTaking)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }
}
