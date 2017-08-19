package ru.android.childdiary.utils.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.services.notifications.NotificationEventService;
import ru.android.childdiary.utils.RingtoneUtils;
import ru.android.childdiary.utils.notifications.core.BaseNotificationHelper;
import ru.android.childdiary.utils.strings.EventUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class EventNotificationHelper extends BaseNotificationHelper {
    @Inject
    public EventNotificationHelper(Context context) {
        super(context);
    }

    public int getNotificationId(@NonNull MasterEvent event) {
        Long masterEventId = event.getMasterEventId();
        return (int) (masterEventId % Integer.MAX_VALUE);
    }

    public void showEventNotification(@NonNull MasterEvent event,
                                      @NonNull EventNotification eventNotification) {
        int notificationId = getNotificationId(event);
        Uri soundUri = eventNotification.getSoundUri();
        soundUri = soundUri == null ? RingtoneUtils.getDefaultNotificationUri() : soundUri;
        String title = EventUtils.getTitle(context, event);
        String text = EventUtils.getDescription(context, event);
        NotificationCompat.Builder builder = createNotificationBuilder();
        builder.setContentIntent(buildPendingIntent(context, notificationId, event))
                .setSmallIcon(ResourcesUtils.getNotificationEventRes(event.getChild()))
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(event.getDateTime().toDate().getTime())
                .setAutoCancel(true)
                .setSound(soundUri);
        if (eventNotification.isVibration()) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(ThemeUtils.getColorPrimary(context, event.getChild() == null ? null : event.getChild().getSex()));
        }
        showNotification(notificationId, builder);
    }

    private PendingIntent buildPendingIntent(Context context,
                                             int requestCode,
                                             @NonNull MasterEvent event) {
        return NotificationEventService.getPendingIntent(requestCode, context, event, false);
    }

    public void hideNotification(@NonNull SleepEvent event) {
        int notificationId = getNotificationId(event);
        hideNotification(notificationId);
    }
}
