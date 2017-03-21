package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.presentation.events.core.EventDetailView;

@InjectViewState
public class OtherEventDetailPresenter extends EventDetailPresenter<EventDetailView<OtherEvent>, OtherEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
