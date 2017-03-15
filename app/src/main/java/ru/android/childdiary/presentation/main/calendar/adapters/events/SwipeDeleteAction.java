package ru.android.childdiary.presentation.main.calendar.adapters.events;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

class SwipeDeleteAction extends SwipeAction {
    public SwipeDeleteAction(@NonNull EventActionListener listener, @NonNull MasterEvent event) {
        super(listener, event);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        listener.delete(event);
    }
}
