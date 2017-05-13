package ru.android.childdiary.presentation.medical.edit.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.requests.DeleteEventsRequest;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.medical.core.BaseEditItemPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class EditMedicineTakingPresenter extends BaseEditItemPresenter<EditMedicineTakingView, MedicineTaking> {
    @Inject
    CalendarInteractor calendarInteractor;

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

    @Override
    public void delete(@NonNull MedicineTaking medicineTaking) {
        if (ObjectUtils.isTrue(medicineTaking.getExported())) {
            getViewState().askDeleteConnectedEventsOrNot(medicineTaking);
        } else {
            deleteMedicineTaking(medicineTaking);
        }
    }

    public void deleteMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(medicineTakingInteractor.deleteMedicineTaking(medicineTaking)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    public void deleteMedicineTakingAndConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(calendarInteractor.delete(DeleteEventsRequest.builder()
                .deleteType(DeleteEventsRequest.DeleteType.DELETE_ALL_MEDICINE_TAKING_EVENTS)
                .medicineTaking(medicineTaking)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> getViewState().deleted(medicineTaking), this::onUnexpectedError));
    }
}
