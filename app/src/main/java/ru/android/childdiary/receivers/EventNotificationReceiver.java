package ru.android.childdiary.receivers;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.receivers.core.BaseReceiver;
import ru.android.childdiary.utils.notifications.EventNotificationHelper;

public class EventNotificationReceiver extends BaseReceiver {
    @Inject
    EventNotificationHelper eventNotificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ApplicationComponent applicationComponent = ChildDiaryApplication.getApplicationComponent();
        applicationComponent.inject(this);

        MasterEvent event = (MasterEvent) intent.getSerializableExtra(ExtraConstants.EXTRA_EVENT);
        MasterEvent defaultEvent = (MasterEvent) intent.getSerializableExtra(ExtraConstants.EXTRA_DEFAULT_EVENT);

        logger.debug("received notification: " + event);

        Long masterEventId = event.getMasterEventId();
        int notificationId = (int) (masterEventId % Integer.MAX_VALUE);
        eventNotificationHelper.showEventNotification(notificationId, event, defaultEvent);
    }
}
