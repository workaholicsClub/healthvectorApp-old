package ru.android.healthvector.presentation.settings.notification;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.calendar.data.core.EventNotification;
import ru.android.healthvector.presentation.core.BaseView;

public interface NotificationView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNotificationSettings(@NonNull EventNotification eventNotification);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void exit(@NonNull EventNotification eventNotification);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmSave(@NonNull EventNotification eventNotification);
}
