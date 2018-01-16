package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeAction;

class SwipeMoveAction extends SwipeAction<MasterEvent, EventActionListener> {
    public SwipeMoveAction(@NonNull MasterEvent event, @NonNull EventActionListener listener) {
        super(event, listener);
    }

    @Override
    protected void doAction(EventActionListener listener, MasterEvent item) {
        listener.move(item);
    }
}
