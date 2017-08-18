package ru.android.childdiary.presentation.settings.notifications.adapters;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.calendar.data.core.EventNotification;

public interface ItemClickedListener {
    void onItemClicked(@NonNull EventNotification eventNotification);
}
