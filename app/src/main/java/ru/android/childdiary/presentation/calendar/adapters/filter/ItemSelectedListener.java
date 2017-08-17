package ru.android.childdiary.presentation.calendar.adapters.filter;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.types.EventType;

interface ItemSelectedListener {
    void onItemSelected(@NonNull EventType eventType, boolean selected);
}
