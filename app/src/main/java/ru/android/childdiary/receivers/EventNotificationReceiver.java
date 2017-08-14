package ru.android.childdiary.receivers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import javax.inject.Inject;

import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
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
        // TODO show notification, log
        long eventId = intent.getLongExtra(ExtraConstants.EXTRA_EVENT_ID, 0);
        Toast.makeText(context, String.valueOf(eventId) + (eventNotificationHelper == null ? " no" : " yes"), Toast.LENGTH_SHORT).show();
    }
}
