package ru.android.childdiary.presentation.main.calendar.adapters;

interface EventActionAdapterListener {
    void delete(EventViewHolder viewHolder);

    void move(EventViewHolder viewHolder);

    void edit(EventViewHolder viewHolder);

    void done(EventViewHolder viewHolder);
}
