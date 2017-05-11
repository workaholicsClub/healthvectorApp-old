package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.presentation.events.core.EventDetailView;

@InjectViewState
public class MedicineTakingEventDetailPresenter extends EventDetailPresenter<EventDetailView<MedicineTakingEvent>, MedicineTakingEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected EventType getEventType() {
        return EventType.MEDICINE_TAKING;
    }
}
