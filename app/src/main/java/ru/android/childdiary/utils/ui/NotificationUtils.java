package ru.android.childdiary.utils.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import org.joda.time.DateTime;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.utils.TimeUtils;

public class NotificationUtils {
    public static NotificationCompat.Builder buildNotification(Context context, SleepEvent event) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        updateNotification(context, builder, event);
        return builder;
    }

    public static void updateNotification(Context context, NotificationCompat.Builder builder, SleepEvent event) {
        builder
                .setSmallIcon(ResourcesUtils.getSleepEventLogoRes(event.getChild().getSex()))
                .setContentTitle(context.getString(R.string.child_sleep, event.getChild().getName()))
                .setWhen(event.getDateTime().toDate().getTime())
                .setContentText(TimeUtils.sleepTime(context, event.getDateTime(), DateTime.now()));
    }

    public static void showNotification(Context context, int notificationId, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    public static void hideNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }
}
