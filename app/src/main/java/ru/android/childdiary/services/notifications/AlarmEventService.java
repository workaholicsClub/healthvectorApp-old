package ru.android.childdiary.services.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ru.android.childdiary.data.types.EventType;
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
                .putExtra(ExtraConstants.EXTRA_EVENT_ID, event.getMasterEventId().longValue())
                .putExtra(ExtraConstants.EXTRA_EVENT_TYPE, event.getEventType().ordinal());
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
        long eventId = intent.getLongExtra(ExtraConstants.EXTRA_EVENT_ID, -1);
        int eventTypeIndex = intent.getIntExtra(ExtraConstants.EXTRA_EVENT_TYPE, -1);
        logger.debug("alarm fired: event id=" + eventId + ", event type index=" + eventTypeIndex);
        if (eventId == -1 || eventTypeIndex < 0 || eventTypeIndex >= EventType.values().length) {
            return;
        }
        EventType eventType = EventType.values()[eventTypeIndex];
        MasterEvent event = calendarInteractor.getEventDetail(eventId, eventType).onErrorReturnItem(MasterEvent.NULL).blockingFirst();
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
