package ru.android.childdiary.services.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;

import javax.inject.Inject;

import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.child.ChildInteractor;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.DoctorVisitEventDetailActivity;
import ru.android.childdiary.presentation.events.ExerciseEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.MedicineTakingEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.presentation.main.AppPartition;
import ru.android.childdiary.presentation.main.MainActivity;
import ru.android.childdiary.services.core.BaseIntentService;
import ru.android.childdiary.utils.notifications.EventNotificationHelper;

public class NotificationEventService extends BaseIntentService {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    EventNotificationHelper eventNotificationHelper;

    public NotificationEventService() {
        super("NotificationEventService");
    }

    private static Intent getServiceIntent(Context context, @NonNull MasterEvent event) {
        return new Intent(context, NotificationEventService.class)
                .putExtra(ExtraConstants.EXTRA_EVENT, event);
    }

    public static void startService(Context context, @NonNull MasterEvent event) {
        Intent intent = getServiceIntent(context, event);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context, @NonNull MasterEvent event) {
        Intent intent = getServiceIntent(context, event);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        MasterEvent event = (MasterEvent) intent.getSerializableExtra(ExtraConstants.EXTRA_EVENT);
        boolean exists = calendarInteractor.exists(event).blockingGet();
        if (exists) {
            MasterEvent defaultEvent = calendarInteractor.getDefaultEvent(event).blockingFirst();
            showEventDetail(this, event, defaultEvent);
        } else {
            logger.debug("event is already deleted: " + event);
            Child child = childInteractor.getActiveChild().blockingFirst();
            showMain(this, child.getSex());
        }
    }

    private void showEventDetail(Context context,
                                 @NonNull MasterEvent event,
                                 @NonNull MasterEvent defaultEvent) {
        Intent intent = getEventDetailIntent(context, event, defaultEvent);
        startActivity(intent);
    }

    private Intent getEventDetailIntent(Context context,
                                        @NonNull MasterEvent event,
                                        @NonNull MasterEvent defaultEvent) {
        switch (event.getEventType()) {
            case DIAPER:
                return wrapEventDetailIntent(
                        DiaperEventDetailActivity.getIntent(context, event, (DiaperEvent) defaultEvent),
                        DiaperEventDetailActivity.class);
            case FEED:
                return wrapEventDetailIntent(
                        FeedEventDetailActivity.getIntent(context, event, (FeedEvent) defaultEvent),
                        FeedEventDetailActivity.class);
            case OTHER:
                return wrapEventDetailIntent(
                        OtherEventDetailActivity.getIntent(context, event, (OtherEvent) defaultEvent),
                        OtherEventDetailActivity.class);
            case PUMP:
                return wrapEventDetailIntent(
                        PumpEventDetailActivity.getIntent(context, event, (PumpEvent) defaultEvent),
                        PumpEventDetailActivity.class);
            case SLEEP:
                return wrapEventDetailIntent(
                        SleepEventDetailActivity.getIntent(context, event, (SleepEvent) defaultEvent),
                        SleepEventDetailActivity.class);
            case DOCTOR_VISIT:
                return wrapEventDetailIntent(
                        DoctorVisitEventDetailActivity.getIntent(context, event, (DoctorVisitEvent) defaultEvent),
                        DoctorVisitEventDetailActivity.class);
            case MEDICINE_TAKING:
                return wrapEventDetailIntent(
                        MedicineTakingEventDetailActivity.getIntent(context, event, (MedicineTakingEvent) defaultEvent),
                        MedicineTakingEventDetailActivity.class);
            case EXERCISE:
                return wrapEventDetailIntent(
                        ExerciseEventDetailActivity.getIntent(context, event, (ExerciseEvent) defaultEvent),
                        ExerciseEventDetailActivity.class);
            default:
                throw new IllegalArgumentException("Unsupported event type");
        }
    }

    private Intent wrapEventDetailIntent(Intent intent, Class<?> activityClass) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void showMain(Context context, @Nullable Sex sex) {
        Intent intent = MainActivity.getIntent(context, AppPartition.CALENDAR, sex);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
