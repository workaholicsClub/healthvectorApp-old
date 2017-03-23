package ru.android.childdiary.utils.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import org.joda.time.DateTime;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.utils.TimeUtils;

public class NotificationUtils {
    private static PendingIntent buildPendingIntent(Context context, int notificationId, @NonNull SleepEvent event) {
        Intent intent = SleepEventDetailActivity.getIntent(context, event, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SleepEventDetailActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static NotificationCompat.Builder buildNotification(Context context, int notificationId, @NonNull SleepEvent event) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(buildPendingIntent(context, notificationId, event));
        updateNotification(context, builder, event);
        return builder;
    }

    public static void updateNotification(Context context, NotificationCompat.Builder builder, @NonNull SleepEvent event) {
        builder
                .setSmallIcon(ResourcesUtils.getNotificationSleepRes(event.getChild().getSex()))
                .setContentTitle(context.getString(R.string.child_sleep, event.getChild().getName()))
                .setWhen(event.getDateTime().toDate().getTime())
                .setContentText(TimeUtils.sleepTime(context, event.getDateTime(), DateTime.now()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(ThemeUtils.getColorPrimary(context, event.getChild().getSex()));
        }
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
