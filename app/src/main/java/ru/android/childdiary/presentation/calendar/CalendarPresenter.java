package ru.android.childdiary.presentation.calendar;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class CalendarPresenter extends AppPartitionPresenter<CalendarView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addDiaperEvent() {
        unsubscribeOnDestroy(calendarInteractor.getDefaultDiaperEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToDiaperEventAdd, this::onUnexpectedError));
    }

    public void addSleepEvent() {
        unsubscribeOnDestroy(calendarInteractor.getDefaultSleepEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToSleepEventAdd, this::onUnexpectedError));
    }

    public void addFeedEvent() {
        unsubscribeOnDestroy(calendarInteractor.getDefaultFeedEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToFeedEventAdd, this::onUnexpectedError));
    }

    public void addPumpEvent() {
        unsubscribeOnDestroy(calendarInteractor.getDefaultPumpEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToPumpEventAdd, this::onUnexpectedError));
    }

    public void addOtherEvent() {
        unsubscribeOnDestroy(calendarInteractor.getDefaultOtherEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToOtherEventAdd, this::onUnexpectedError));
    }
}
