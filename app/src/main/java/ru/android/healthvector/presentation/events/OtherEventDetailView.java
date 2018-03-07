package ru.android.healthvector.presentation.events;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.presentation.events.core.EventDetailView;

public interface OtherEventDetailView extends EventDetailView<OtherEvent> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void otherEventNameValidated(boolean valid);
}
