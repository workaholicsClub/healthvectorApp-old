package ru.android.childdiary.services.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
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

    private static Intent getServiceIntent(Context context,
                                           @NonNull MasterEvent event,
                                           boolean isLinearGroupFinished) {
        return new Intent(context, NotificationEventService.class)
                .putExtra(ExtraConstants.EXTRA_EVENT, event)
                .putExtra(ExtraConstants.EXTRA_IS_LINEAR_GROUP_FINISHED, isLinearGroupFinished);
    }

    public static PendingIntent getPendingIntent(int requestCode,
                                                 Context context,
                                                 @NonNull MasterEvent event,
                                                 boolean isLinearGroupFinished) {
        Intent intent = getServiceIntent(context, event, isLinearGroupFinished)
                .setAction(String.valueOf(SystemClock.elapsedRealtime()));
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
        logger.debug("notification clicked: " + event);
        if (event == null) {
            return;
        }
        boolean isLinearGroupFinished = intent.getBooleanExtra(ExtraConstants.EXTRA_IS_LINEAR_GROUP_FINISHED, false);
        logger.debug("notification clicked: " + event + "; linear group finished: " + isLinearGroupFinished);
        event = calendarInteractor.getEventDetail(event).onErrorReturnItem(MasterEvent.NULL).blockingFirst();
        if (event == MasterEvent.NULL) {
            logger.debug("event is already deleted");
            Child child = childInteractor.getActiveChild().blockingFirst();
            showMain(this, child.getSex());
        } else {
            MasterEvent defaultEvent = calendarInteractor.getDefaultEvent(event).blockingFirst();
            showEventDetail(this, event, defaultEvent, isLinearGroupFinished);
        }
    }

    private void showEventDetail(Context context,
                                 @NonNull MasterEvent event,
                                 @NonNull MasterEvent defaultEvent,
                                 boolean isLinearGroupFinished) {
        TaskStackBuilder stackBuilder = getEventDetailStackBuilder(context, event, defaultEvent, isLinearGroupFinished);
        stackBuilder.startActivities();
    }

    private TaskStackBuilder getEventDetailStackBuilder(Context context,
                                                        @NonNull MasterEvent event,
                                                        @NonNull MasterEvent defaultEvent,
                                                        boolean isLinearGroupFinished) {
        switch (event.getEventType()) {
            case DIAPER:
                return getEventDetailStackBuilder(
                        DiaperEventDetailActivity.getIntent(context, event, (DiaperEvent) defaultEvent),
                        DiaperEventDetailActivity.class);
            case FEED:
                return getEventDetailStackBuilder(
                        FeedEventDetailActivity.getIntent(context, event, (FeedEvent) defaultEvent),
                        FeedEventDetailActivity.class);
            case OTHER:
                return getEventDetailStackBuilder(
                        OtherEventDetailActivity.getIntent(context, event, (OtherEvent) defaultEvent),
                        OtherEventDetailActivity.class);
            case PUMP:
                return getEventDetailStackBuilder(
                        PumpEventDetailActivity.getIntent(context, event, (PumpEvent) defaultEvent),
                        PumpEventDetailActivity.class);
            case SLEEP:
                return getEventDetailStackBuilder(
                        SleepEventDetailActivity.getIntent(context, event, (SleepEvent) defaultEvent),
                        SleepEventDetailActivity.class);
            case DOCTOR_VISIT:
                return getEventDetailStackBuilder(
                        DoctorVisitEventDetailActivity.getIntent(context, event, (DoctorVisitEvent) defaultEvent, isLinearGroupFinished),
                        DoctorVisitEventDetailActivity.class);
            case MEDICINE_TAKING:
                return getEventDetailStackBuilder(
                        MedicineTakingEventDetailActivity.getIntent(context, event, (MedicineTakingEvent) defaultEvent, isLinearGroupFinished),
                        MedicineTakingEventDetailActivity.class);
            case EXERCISE:
                return getEventDetailStackBuilder(
                        ExerciseEventDetailActivity.getIntent(context, event, (ExerciseEvent) defaultEvent, isLinearGroupFinished),
                        ExerciseEventDetailActivity.class);
            default:
                throw new IllegalArgumentException("Unsupported event type");
        }
    }

    private TaskStackBuilder getEventDetailStackBuilder(Intent intent, Class<?> activityClass) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(intent);
        return stackBuilder;
    }

    private void showMain(Context context, @Nullable Sex sex) {
        Intent intent = MainActivity.getIntent(context, AppPartition.CALENDAR, sex);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
