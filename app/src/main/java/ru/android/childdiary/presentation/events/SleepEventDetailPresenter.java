package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;

@InjectViewState
public class SleepEventDetailPresenter extends EventDetailPresenter<SleepEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
