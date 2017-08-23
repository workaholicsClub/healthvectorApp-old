package ru.android.childdiary.utils.notifications.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public abstract class BaseNotificationHelper {
    private static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "CEV";
    private static final String CHANNEL_DESCRIPTION = "CEV application";

    private static boolean isChannelInitialized;

    protected final Context context;
    protected final NotificationManager notificationManager;

    protected BaseNotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    protected final NotificationCompat.Builder createNotificationBuilder() {
        initChannel();
        return new NotificationCompat.Builder(context, CHANNEL_ID);
    }

    private void initChannel() {
        synchronized (BaseNotificationHelper.class) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return;
            }
            if (isChannelInitialized) {
                return;
            }
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
            isChannelInitialized = true;
        }
    }

    protected void showNotification(int notificationId, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    protected void hideNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }
}
