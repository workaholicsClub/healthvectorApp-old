package ru.android.healthvector.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.standard.DiaperEvent;
import ru.android.healthvector.presentation.events.core.EventDetailPresenter;
import ru.android.healthvector.presentation.events.core.EventDetailView;

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
