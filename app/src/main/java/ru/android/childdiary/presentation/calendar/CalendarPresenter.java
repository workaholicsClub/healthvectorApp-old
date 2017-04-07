package ru.android.childdiary.presentation.calendar;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class CalendarPresenter extends AppPartitionPresenter<CalendarView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addDiaperEvent() {
        getViewState().navigateToDiaperEventAdd();
    }

    public void addSleepEvent() {
        getViewState().navigateToSleepEventAdd();
    }

    public void addFeedEvent() {
        getViewState().navigateToFeedEventAdd();
    }

    public void addPumpEventClick() {
        getViewState().navigateToPumpEventAdd();
    }

    public void addOtherEventClick() {
        getViewState().navigateToOtherEventAdd();
    }
}
