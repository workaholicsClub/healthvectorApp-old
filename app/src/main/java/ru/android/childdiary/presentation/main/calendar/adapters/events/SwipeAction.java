package ru.android.childdiary.presentation.main.calendar.adapters.events;

import com.daimajia.swipe.SwipeLayout;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

class SwipeAction implements SwipeLayout.SwipeListener {
    protected final EventActionListener listener;
    protected final MasterEvent event;

    public SwipeAction(EventActionListener listener, MasterEvent event) {
        this.listener = listener;
        this.event = event;
    }

    @Override
    public void onStartOpen(SwipeLayout layout) {
    }

    @Override
    public void onOpen(SwipeLayout layout) {
    }

    @Override
    public void onStartClose(SwipeLayout layout) {
    }

    @Override
    public void onClose(SwipeLayout layout) {
    }

    @Override
    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
    }

    @Override
    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
    }
}
