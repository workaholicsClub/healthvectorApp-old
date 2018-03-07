package ru.android.healthvector.presentation.calendar.adapters.events;

import ru.android.healthvector.presentation.core.adapters.swipe.SwipeActionListener;

interface EventSwipeActionListener extends SwipeActionListener<EventViewHolder> {
    void move(EventViewHolder viewHolder);

    void done(EventViewHolder viewHolder);
}
