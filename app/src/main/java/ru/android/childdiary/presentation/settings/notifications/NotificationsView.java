package ru.android.childdiary.presentation.settings.notifications;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;

public interface NotificationsView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showNotificationSettings(@NonNull List<EventNotification> eventNotifications);
}
