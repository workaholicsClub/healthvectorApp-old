package ru.android.childdiary.presentation.events.core;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;

public abstract class PeriodicEventDetailPresenter<V extends PeriodicEventDetailView<T>, T extends MasterEvent>
        extends EventDetailPresenter<V, T> {
    public void requestLengthValueDialog() {
        unsubscribeOnDestroy(calendarInteractor.getTimeUnitValues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showLengthValueDialog, this::onUnexpectedError));
    }
}
