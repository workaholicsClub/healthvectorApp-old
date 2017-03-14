package ru.android.childdiary.presentation.main.calendar.adapters.events;

import com.daimajia.swipe.SwipeLayout;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

class SwipeEditAction extends SwipeAction {
    public SwipeEditAction(EventActionListener listener, MasterEvent event) {
        super(listener, event);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        if (listener != null) {
            listener.edit(event);
        }
    }
}
