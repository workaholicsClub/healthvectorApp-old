package ru.android.childdiary.services.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.services.core.BaseIntentService;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.notifications.EventNotificationHelper;

public class AlarmEventService extends BaseIntentService {
    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    EventNotificationHelper eventNotificationHelper;

    public AlarmEventService() {
        super("AlarmEventService");
    }

    private static Intent getServiceIntent(Context context, @NonNull MasterEvent event) {
        return new Intent(context, AlarmEventService.class)
                .putExtra(ExtraConstants.EXTRA_EVENT, event);
    }

    public static void startService(Context context, @NonNull MasterEvent event) {
        Intent intent = getServiceIntent(context, event);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context, @NonNull MasterEvent event) {
        Intent intent = getServiceIntent(context, event);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        DateTime now = DateTime.now();
        MasterEvent event = (MasterEvent) intent.getSerializableExtra(ExtraConstants.EXTRA_EVENT);
        boolean exists = calendarInteractor.exists(event).blockingGet();
        if (exists) {
            boolean inTime = ObjectUtils.equalsToMinutes(now, event.getNotifyDateTime());
            if (inTime) {
                MasterEvent defaultEvent = calendarInteractor.getDefaultEvent(event).blockingFirst();
                EventNotification eventNotification = calendarInteractor.getNotificationSettings(event.getEventType()).blockingFirst();
                eventNotificationHelper.showEventNotification(event, eventNotification);
            } else {
                logger.debug("event is expired: " + event);
            }
        } else {
            logger.debug("event is already deleted: " + event);
        }
    }
}
