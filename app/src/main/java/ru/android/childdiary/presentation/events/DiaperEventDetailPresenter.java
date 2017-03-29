package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.presentation.events.core.EventDetailView;

@InjectViewState
public class DiaperEventDetailPresenter extends EventDetailPresenter<EventDetailView<DiaperEvent>, DiaperEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected EventType getEventType() {
        return EventType.DIAPER;
    }
}
