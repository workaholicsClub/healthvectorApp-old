package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;

import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;

class SwipeDoneAction extends SwipeAction {
    public SwipeDoneAction(@NonNull EventActionListener listener, @NonNull MasterEvent event) {
        super(listener, event);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        listener.done(event);
    }
}
