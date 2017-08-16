package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.db.exceptions.DowngradeDatabaseException;
import ru.android.childdiary.data.services.ScheduleHelper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.requests.GetEventsFilter;
import ru.android.childdiary.domain.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.services.core.BaseService;
import ru.android.childdiary.services.notifications.AlarmEventService;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.notifications.EventNotificationHelper;
import ru.android.childdiary.utils.strings.DateUtils;

public class EventScheduleService extends BaseService {
    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    ScheduleHelper scheduleHelper;

    @Inject
    EventNotificationHelper eventNotificationHelper;

    private Disposable subscription;

    private static Intent getServiceIntent(Context context) {
        Intent intent = new Intent(context, EventScheduleService.class);
        intent.setAction(String.valueOf(SystemClock.elapsedRealtime()));
        return intent;
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context) {
        Intent intent = getServiceIntent(context);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static boolean isInTheFuture(@NonNull MasterEvent event) {
        return event.getMasterEventId() != null
                && event.getNotifyDateTime() != null
                && event.getNotifyDateTime().getMillis() >= now();
    }

    private static long now() {
        return DateTime.now().withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
        subscribeOnUpdates();
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        LocalDate today = LocalDate.now();
        DateTime midnight = DateUtils.nextDayMidnight(today);
        scheduleHelper.installAlarm(getPendingIntent(0, this), midnight.getMillis());
        subscribeOnUpdates();
    }

    private void subscribeOnUpdates() {
        unsubscribe(subscription);
        try {
            subscription = unsubscribeOnDestroy(
                    calendarInteractor.getAll(
                            GetEventsRequest.builder()
                                    .date(LocalDate.now())
                                    .filter(GetEventsFilter.builder()
                                            .eventTypes(Arrays.asList(EventType.values()))
                                            .build())
                                    .child(Child.NULL)
                                    .build())
                            .map(GetEventsResponse::getEvents)
                            .flatMap(Observable::fromIterable)
                            .filter(EventScheduleService::isInTheFuture)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::handleResult, this::onUnexpectedError));
        } catch (DowngradeDatabaseException e) {
            logger.error("Can't subscribe to updates", e);
        }
    }

    private void handleResult(@NonNull MasterEvent event) {
        logger.debug("schedule event: " + event);
        int requestCode = eventNotificationHelper.getNotificationId(event);
        PendingIntent pendingIntent = AlarmEventService.getPendingIntent(requestCode, this, event);
        long time = event.getNotifyDateTime().withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
        scheduleHelper.installAlarm(pendingIntent, time);
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
    }
}
