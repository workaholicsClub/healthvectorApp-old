package ru.android.childdiary.presentation.settings.notifications.adapters;

import ru.android.childdiary.domain.calendar.data.core.EventNotification;

public interface ItemClickedListener {
    void onItemClicked(EventNotification eventNotification);
}
