package ru.android.childdiary.presentation.medical.edit.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.DateTime;

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
        showProgressUpdate(medicineTaking);
        unsubscribeOnDestroy(
                medicineTakingInteractor.updateMedicineTaking(medicineTaking)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .doOnNext(added -> hideProgressUpdate(medicineTaking))
                        .doOnError(throwable -> hideProgressUpdate(medicineTaking))
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }

    private void showProgressUpdate(@NonNull MedicineTaking medicineTaking) {
        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            getViewState().showGeneratingEvents(true);
        }
    }

    private void hideProgressUpdate(@NonNull MedicineTaking medicineTaking) {
        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            getViewState().showGeneratingEvents(false);
        }
    }

    @Override
    public void delete(@NonNull MedicineTaking medicineTaking) {
        if (ObjectUtils.isTrue(medicineTaking.getIsExported())) {
            getViewState().askDeleteConnectedEventsOrNot(medicineTaking);
        } else {
            deleteOneItem(medicineTaking);
        }
    }

    @Override
    public void deleteOneItem(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(medicineTakingInteractor.deleteMedicineTaking(medicineTaking)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    @Override
    public void deleteWithConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(calendarInteractor.delete(DeleteEventsRequest.builder()
                .deleteType(DeleteEventsRequest.DeleteType.DELETE_ALL_MEDICINE_TAKING_EVENTS)
                .medicineTaking(medicineTaking)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(count -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(count -> getViewState().deleted(medicineTaking), this::onUnexpectedError));
    }

    @Override
    public void complete(@NonNull MedicineTaking item) {
        if (item.getFinishDateTime() == null) {
            getViewState().askCompleteFromDate(item, DateTime.now());
        } else {
            logger.error("already completed: " + item);
        }
    }

    @Override
    public void completeWithoutDeletion(@NonNull MedicineTaking medicineTaking, @NonNull DateTime dateTime) {
        unsubscribeOnDestroy(calendarInteractor.delete(DeleteEventsRequest.builder()
                .deleteType(DeleteEventsRequest.DeleteType.COMPLETE_MEDICINE_TAKING)
                .medicineTaking(medicineTaking)
                .dateTime(dateTime)
                .delete(false)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> getViewState().completed(medicineTaking), this::onUnexpectedError));
    }

    @Override
    public void completeAndDeleteFromDate(@NonNull MedicineTaking medicineTaking, @NonNull DateTime dateTime) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(calendarInteractor.delete(DeleteEventsRequest.builder()
                .deleteType(DeleteEventsRequest.DeleteType.COMPLETE_MEDICINE_TAKING)
                .medicineTaking(medicineTaking)
                .dateTime(dateTime)
                .delete(true)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(count -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(count -> getViewState().completed(medicineTaking), this::onUnexpectedError));
    }
}
