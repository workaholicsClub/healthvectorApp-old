package ru.android.healthvector.utils.notifications.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public abstract class BaseNotificationHelper {
    protected final Context context;
    protected final NotificationManager notificationManager;

    protected BaseNotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    protected final NotificationCompat.Builder createNotificationBuilder() {
        return new NotificationCompat.Builder(context);
    }

    protected void showNotification(int notificationId, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    protected void hideNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }
}
