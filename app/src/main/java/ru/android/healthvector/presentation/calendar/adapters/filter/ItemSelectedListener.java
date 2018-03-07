package ru.android.healthvector.presentation.calendar.adapters.filter;

import android.support.annotation.NonNull;

import ru.android.healthvector.data.types.EventType;

interface ItemSelectedListener {
    void onItemSelected(@NonNull EventType eventType, boolean selected);
}
