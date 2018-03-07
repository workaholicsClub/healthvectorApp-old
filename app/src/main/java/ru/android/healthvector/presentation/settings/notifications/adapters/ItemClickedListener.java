package ru.android.healthvector.presentation.settings.notifications.adapters;

import android.support.annotation.NonNull;

import ru.android.healthvector.domain.calendar.data.core.EventNotification;

public interface ItemClickedListener {
    void onItemClicked(@NonNull EventNotification eventNotification);
}
