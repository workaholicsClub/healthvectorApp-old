package ru.android.healthvector.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeAction;

class SwipeDoneAction extends SwipeAction<MasterEvent, EventActionListener> {
    public SwipeDoneAction(@NonNull MasterEvent event, @NonNull EventActionListener listener) {
        super(event, listener);
    }

    @Override
    protected void doAction(EventActionListener listener, MasterEvent item) {
        listener.done(item);
    }
}
