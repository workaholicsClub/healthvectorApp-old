package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;

public abstract class PeriodicEventDetailPresenter<V extends PeriodicEventDetailView<T>, T extends MasterEvent>
        extends EventDetailPresenter<V, T> {
    public void requestLengthValueDialog(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(calendarInteractor.getTimeUnitValues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showLengthValueDialog, this::onUnexpectedError));
    }

    public final void continueLinearGroup(@NonNull T event) {
        getViewState().showGeneratingEvents(true);
        unsubscribeOnDestroy(
                generateEvents(event)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(added -> logger.debug("added: " + added))
                        .doOnNext(added -> getViewState().showGeneratingEvents(false))
                        .doOnError(throwable -> getViewState().showGeneratingEvents(false))
                        .subscribe(getViewState()::eventsAdded, this::onUnexpectedError));
    }

    protected abstract Observable<Integer> generateEvents(@NonNull T event);
}
