package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeAction;

class SwipeDoneAction extends SwipeAction<MasterEvent, EventActionListener> {
    public SwipeDoneAction(@NonNull MasterEvent event, @NonNull EventActionListener listener) {
        super(event, listener);
    }

    @Override
    protected void doAction(EventActionListener listener, MasterEvent item) {
        listener.done(item);
    }
}
