package ru.android.childdiary.presentation.medical.edit.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.DateTime;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertMedicineTakingRequest;
import ru.android.childdiary.presentation.core.events.BaseEditItemPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class EditMedicineTakingPresenter extends BaseEditItemPresenter<EditMedicineTakingView, MedicineTaking> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void update(@NonNull MedicineTaking medicineTaking) {
        showProgressUpdate(medicineTaking);
        unsubscribeOnDestroy(
                medicineTakingInteractor.updateMedicineTaking(UpsertMedicineTakingRequest.builder()
                        .medicineTaking(medicineTaking)
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .doOnNext(updated -> hideProgressUpdate(medicineTaking))
                        .doOnError(throwable -> hideProgressUpdate(medicineTaking))
                        .subscribe(response -> getViewState().updated(response.getMedicineTaking(), response.getAddedEventsCount()),
                                this::onUnexpectedError));
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
            unsubscribeOnDestroy(medicineTakingInteractor.hasConnectedEvents(medicineTaking)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(hasConnectedEvents -> {
                        if (hasConnectedEvents) {
                            getViewState().askDeleteConnectedEventsOrNot(medicineTaking);
                        } else {
                            getViewState().confirmDeleteOneItem(medicineTaking);
                        }
                    }, this::onUnexpectedError));
        } else {
            getViewState().confirmDeleteOneItem(medicineTaking);
        }
    }

    @Override
    public void deleteOneItem(@NonNull MedicineTaking medicineTaking) {
        unsubscribeOnDestroy(medicineTakingInteractor.deleteMedicineTaking(DeleteMedicineTakingRequest.builder()
                .medicineTaking(medicineTaking)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> getViewState().deleted(medicineTaking), this::onUnexpectedError));
    }

    @Override
    public void deleteWithConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(medicineTakingInteractor.deleteMedicineTakingWithEvents(DeleteMedicineTakingEventsRequest.builder()
                .medicineTaking(medicineTaking)
                .linearGroup(null)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(response -> getViewState().deleted(medicineTaking), this::onUnexpectedError));
    }

    @Override
    public void complete(@NonNull MedicineTaking item) {
        if (!item.isDone()) {
            getViewState().askCompleteFromDate(item, DateTime.now());
        } else {
            logger.error("already completed: " + item);
        }
    }

    @Override
    public void completeWithoutDeletion(@NonNull MedicineTaking medicineTaking, @NonNull DateTime dateTime) {
        unsubscribeOnDestroy(medicineTakingInteractor.completeMedicineTaking(CompleteMedicineTakingRequest.builder()
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
        unsubscribeOnDestroy(medicineTakingInteractor.completeMedicineTaking(CompleteMedicineTakingRequest.builder()
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

    @Override
    protected EventType getEventType() {
        return EventType.MEDICINE_TAKING;
    }
}
