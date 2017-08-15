package ru.android.childdiary.utils.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.notifications.core.BaseNotificationHelper;

public class CloudNotificationHelper extends BaseNotificationHelper {
    @Inject
    public CloudNotificationHelper(Context context) {
        super(context);
    }

    public void showBackupErrorNotification(int notificationId,
                                            String title,
                                            String text,
                                            @Nullable PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = createNotificationBuilder();
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        builder.setSmallIcon(R.drawable.ic_stat_autobackup_error)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        showNotification(notificationId, builder);
    }

    public void showBackupProgressNotification(int notificationId) {
        String title = context.getString(R.string.app_name);
        String text = context.getString(R.string.notification_text_backup);
        NotificationCompat.Builder builder = createNotificationBuilder();
        builder.setSmallIcon(R.drawable.ic_stat_autobackup)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setProgress(0, 0, true);
        showNotification(notificationId, builder);
    }
}
