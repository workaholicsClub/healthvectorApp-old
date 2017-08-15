package ru.android.childdiary.utils.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.services.ServiceController;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class SleepNotificationHelper extends BaseNotificationHelper {
    private final ServiceController serviceController;

    @Inject
    public SleepNotificationHelper(Context context, ServiceController serviceController) {
        super(context);
        this.serviceController = serviceController;
    }

    public NotificationCompat.Builder buildSleepNotification(int notificationId,
                                                             @NonNull SleepEvent event,
                                                             @NonNull SleepEvent defaultEvent) {
        NotificationCompat.Builder builder = createNotificationBuilder();
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
            builder.setColor(ThemeUtils.getColorPrimary(context, event.getChild() == null ? null : event.getChild().getSex()));
        }
    }

    @Override
    public void showNotification(int notificationId, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(notificationId, notification);
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
        return TimerService.getPendingIntent(0, context, TimerService.ACTION_STOP_SLEEP_EVENT_TIMER, event);
    }
}
