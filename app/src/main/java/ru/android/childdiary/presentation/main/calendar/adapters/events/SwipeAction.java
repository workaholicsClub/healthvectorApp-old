package ru.android.childdiary.presentation.main.calendar.adapters.events;

import com.daimajia.swipe.SimpleSwipeListener;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

class SwipeAction extends SimpleSwipeListener {
    protected final EventActionListener listener;
    protected final MasterEvent event;

    public SwipeAction(EventActionListener listener, MasterEvent event) {
        this.listener = listener;
        this.event = event;
    }
}
