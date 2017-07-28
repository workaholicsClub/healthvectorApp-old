package ru.android.childdiary.presentation.calendar.adapters.filter;

import ru.android.childdiary.data.types.EventType;

interface ItemSelectedListener {
    void onItemSelected(EventType eventType, boolean selected);
}
