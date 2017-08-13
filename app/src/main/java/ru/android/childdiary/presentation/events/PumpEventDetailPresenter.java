package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.presentation.events.core.EventDetailView;

@InjectViewState
public class PumpEventDetailPresenter extends EventDetailPresenter<EventDetailView<PumpEvent>, PumpEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected EventType getEventType() {
        return EventType.PUMP;
    }
}
