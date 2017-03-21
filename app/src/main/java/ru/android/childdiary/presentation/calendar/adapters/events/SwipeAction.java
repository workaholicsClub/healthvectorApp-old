package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SimpleSwipeListener;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

class SwipeAction extends SimpleSwipeListener {
    protected final EventActionListener listener;
    protected final MasterEvent event;

    public SwipeAction(@NonNull EventActionListener listener, @NonNull MasterEvent event) {
        this.listener = listener;
        this.event = event;
    }
}
