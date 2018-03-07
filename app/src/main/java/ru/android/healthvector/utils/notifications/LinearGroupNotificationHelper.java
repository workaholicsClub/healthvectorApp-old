package ru.android.healthvector.utils.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.EventNotification;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;
import ru.android.healthvector.services.notifications.NotificationEventService;
import ru.android.healthvector.utils.RingtoneUtils;
import ru.android.healthvector.utils.notifications.core.BaseNotificationHelper;
import ru.android.healthvector.utils.strings.EventUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class LinearGroupNotificationHelper extends BaseNotificationHelper {
    @Inject
    public LinearGroupNotificationHelper(Context context) {
        super(context);
    }

    public int getNotificationId(@NonNull MasterEvent event) {
        Long masterEventId = event.getMasterEventId();
        // ИД -1 и -2 заняты для автоматического бэкапа
        return -2 - (int) (masterEventId % Integer.MAX_VALUE);
    }

    public void showEventNotification(@NonNull MasterEvent event,
                                      @NonNull EventNotification eventNotification) {
        int notificationId = getNotificationId(event);
        Uri soundUri = eventNotification.getSoundUri();
        soundUri = soundUri == null ? RingtoneUtils.getDefaultNotificationUri() : soundUri;
        String title = context.getString(R.string.linear_group_finished_notification_title);
        String text = EventUtils.getTitleAndDescription(context, event);
        NotificationCompat.Builder builder = createNotificationBuilder();
        builder.setContentIntent(buildPendingIntent(context, notificationId, event))
                .setSmallIcon(ResourcesUtils.getNotificationLinearGroupRes(event.getChild()))
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
        return NotificationEventService.getPendingIntent(requestCode, context, event, true);
    }

    public void hideNotification(@NonNull SleepEvent event) {
        int notificationId = getNotificationId(event);
        hideNotification(notificationId);
    }
}
