package ru.android.childdiary.utils.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.notifications.core.BaseNotificationHelper;

public class CloudNotificationHelper extends BaseNotificationHelper {
    private static final int NOTIFICATION_ID_ERROR = -1;
    private static final int NOTIFICATION_ID_BACKUP = -2;

    @Inject
    public CloudNotificationHelper(Context context) {
        super(context);
    }

    public void showBackupErrorNotification(String title,
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
        showNotification(NOTIFICATION_ID_ERROR, builder);
    }

    public void showBackupProgressNotification() {
        String title = context.getString(R.string.app_name);
        String text = context.getString(R.string.notification_text_backup);
        NotificationCompat.Builder builder = createNotificationBuilder();
        builder.setSmallIcon(R.drawable.ic_stat_autobackup)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setProgress(0, 0, true);
        showNotification(NOTIFICATION_ID_BACKUP, builder);
    }

    public void hideBackupProgressNotification() {
        hideNotification(NOTIFICATION_ID_BACKUP);
    }
}
