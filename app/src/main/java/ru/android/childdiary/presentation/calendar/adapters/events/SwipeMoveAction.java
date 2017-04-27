package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;

import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;

class SwipeMoveAction extends SwipeAction {
    public SwipeMoveAction(@NonNull EventActionListener listener, @NonNull MasterEvent event) {
        super(listener, event);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        layout.removeSwipeListener(this);
        listener.move(event);
    }
}
