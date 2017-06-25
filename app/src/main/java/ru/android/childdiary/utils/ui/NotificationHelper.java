package ru.android.childdiary.utils.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.utils.TimeUtils;

public class NotificationHelper {
    private final Context context;
    private final NotificationManager notificationManager;

    @Inject
    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent buildSleepPendingIntent(int notificationId,
                                                  @NonNull SleepEvent event,
                                                  @NonNull SleepEvent defaultEvent) {
        Intent intent = SleepEventDetailActivity.getIntent(context, event, defaultEvent);
        intent.setAction(String.valueOf(SystemClock.elapsedRealtime()));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SleepEventDetailActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent buildSleepActionPendingIntent(int notificationId,
                                                        @NonNull SleepEvent event) {
        Intent intent = new Intent(context, TimerService.class);
        intent.setAction(String.valueOf(SystemClock.elapsedRealtime()));
        intent.putExtra(TimerService.EXTRA_ACTION, TimerService.ACTION_STOP_SLEEP_EVENT_TIMER);
        intent.putExtra(ExtraConstants.EXTRA_EVENT, event);
        PendingIntent pendingIntent = PendingIntent.getService(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public NotificationCompat.Builder buildSleepNotification(int notificationId,
                                                             @NonNull SleepEvent event,
                                                             @NonNull SleepEvent defaultEvent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(buildSleepPendingIntent(notificationId, event, defaultEvent))
                .addAction(R.drawable.ic_action_stop_sleep_timer,
                        context.getString(R.string.stop_sleep_timer),
                        buildSleepActionPendingIntent(notificationId, event));
        updateSleepNotification(builder, event);
        return builder;
    }

    public void updateSleepNotification(NotificationCompat.Builder builder,
                                        @NonNull SleepEvent event) {
        String contentTitle, contentText;
        DateTime now = DateTime.now();
        DateTime start = event.getDateTime();
        if (now.isAfter(start) || now.isEqual(start)) {
            contentTitle = context.getString(R.string.child_sleep,
                    event.getChild() == null ? null : event.getChild().getName());
            contentText = TimeUtils.timerString(context, start, now);
        } else {
            contentTitle = context.getString(R.string.sleep_timer);
            String duration = TimeUtils.timerString(context, now, start);
            contentText = context.getString(R.string.will_start, duration);
        }
        builder.setSmallIcon(ResourcesUtils.getNotificationSleepRes(event.getChild()))
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(event.getDateTime().toDate().getTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(ThemeUtils.getColorPrimary(context, event.getChild().getSex()));
        }
    }

    public void showSleepNotification(int notificationId, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(notificationId, notification);
    }

    public void hideNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

    public void showBackupErrorNotification(int notificationId,
                                            String title,
                                            String text,
                                            @Nullable PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        builder.setSmallIcon(R.drawable.ic_stat_autobackup_error)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    public void showBackupProgressNotification(int notificationId) {
        String title = context.getString(R.string.app_name);
        String text = context.getString(R.string.notification_text_backup);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_stat_autobackup)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setProgress(0, 0, true);
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }
}
