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
        logger.debug("alarm fired: " + event);
        if (event == null) {
            return;
        }
        event = calendarInteractor.getEventDetail(event).onErrorReturnItem(MasterEvent.NULL).blockingFirst();
        if (event == MasterEvent.NULL) {
            logger.debug("event is already deleted");
        } else {
            boolean inTime = ObjectUtils.equalsToMinutes(now, event.getNotifyDateTime());
            if (inTime) {
                EventNotification eventNotification = calendarInteractor.getNotificationSettings(event.getEventType()).blockingFirst();
                eventNotificationHelper.showEventNotification(event, eventNotification);
            } else {
                logger.debug("notify time changed");
            }
        }
    }
}
