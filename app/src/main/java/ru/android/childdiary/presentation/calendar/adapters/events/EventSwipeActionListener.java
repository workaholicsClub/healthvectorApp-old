package ru.android.childdiary.presentation.calendar.adapters.events;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeActionListener;

interface EventSwipeActionListener extends SwipeActionListener<EventViewHolder> {
    void move(EventViewHolder viewHolder);

    void done(EventViewHolder viewHolder);
}
