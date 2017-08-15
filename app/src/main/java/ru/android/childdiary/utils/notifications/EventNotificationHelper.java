package ru.android.childdiary.utils.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import javax.inject.Inject;

import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.DoctorVisitEventDetailActivity;
import ru.android.childdiary.presentation.events.ExerciseEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.MedicineTakingEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.utils.strings.EventUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class EventNotificationHelper extends BaseNotificationHelper {
    @Inject
    public EventNotificationHelper(Context context) {
        super(context);
    }

    public void showEventNotification(int notificationId,
                                      @NonNull MasterEvent event,
                                      @NonNull MasterEvent defaultEvent) {
        String title = EventUtils.getTitle(context, event);
        String text = EventUtils.getDescription(context, event);
        NotificationCompat.Builder builder = createNotificationBuilder();
        builder.setContentIntent(buildPendingIntent(context, notificationId, event, defaultEvent))
                .setSmallIcon(ResourcesUtils.getNotificationEventRes(event.getChild()))
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(event.getDateTime().toDate().getTime())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(ThemeUtils.getColorPrimary(context, event.getChild() == null ? null : event.getChild().getSex()));
        }
        showNotification(notificationId, builder);
    }

    public PendingIntent buildPendingIntent(Context context,
                                            int notificationId,
                                            @NonNull MasterEvent event,
                                            @NonNull MasterEvent defaultEvent) {
        switch (event.getEventType()) {
            case DIAPER:
                return buildPendingIntent(context, notificationId,
                        DiaperEventDetailActivity.getIntent(context, event, (DiaperEvent) defaultEvent),
                        DiaperEventDetailActivity.class);
            case FEED:
                return buildPendingIntent(context, notificationId,
                        FeedEventDetailActivity.getIntent(context, event, (FeedEvent) defaultEvent),
                        FeedEventDetailActivity.class);
            case OTHER:
                return buildPendingIntent(context, notificationId,
                        OtherEventDetailActivity.getIntent(context, event, (OtherEvent) defaultEvent),
                        OtherEventDetailActivity.class);
            case PUMP:
                return buildPendingIntent(context, notificationId,
                        PumpEventDetailActivity.getIntent(context, event, (PumpEvent) defaultEvent),
                        PumpEventDetailActivity.class);
            case SLEEP:
                return buildPendingIntent(context, notificationId,
                        FeedEventDetailActivity.getIntent(context, event, (FeedEvent) defaultEvent),
                        FeedEventDetailActivity.class);
            case DOCTOR_VISIT:
                return buildPendingIntent(context, notificationId,
                        DoctorVisitEventDetailActivity.getIntent(context, event, (DoctorVisitEvent) defaultEvent),
                        DoctorVisitEventDetailActivity.class);
            case MEDICINE_TAKING:
                return buildPendingIntent(context, notificationId,
                        MedicineTakingEventDetailActivity.getIntent(context, event, (MedicineTakingEvent) defaultEvent),
                        MedicineTakingEventDetailActivity.class);
            case EXERCISE:
                return buildPendingIntent(context, notificationId,
                        ExerciseEventDetailActivity.getIntent(context, event, (ExerciseEvent) defaultEvent),
                        ExerciseEventDetailActivity.class);
            default:
                throw new IllegalArgumentException("Unsupported event type");
        }
    }

    private PendingIntent buildPendingIntent(Context context,
                                             int notificationId,
                                             @NonNull Intent intent,
                                             @NonNull Class<?> activityClass) {
        intent.setAction(String.valueOf(SystemClock.elapsedRealtime()));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
