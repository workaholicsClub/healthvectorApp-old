package ru.android.healthvector.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
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
import ru.android.healthvector.data.services.ScheduleHelper;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.CalendarInteractor;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.requests.GetEventsFilter;
import ru.android.healthvector.domain.calendar.requests.GetEventsRequest;
import ru.android.healthvector.domain.calendar.requests.GetEventsResponse;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.services.core.BaseService;
import ru.android.healthvector.services.notifications.AlarmEventService;
import ru.android.healthvector.utils.log.LogSystem;
import ru.android.healthvector.utils.notifications.EventNotificationHelper;
import ru.android.healthvector.utils.strings.DateUtils;

public class EventScheduleService extends BaseService {
    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    ScheduleHelper scheduleHelper;

    @Inject
    EventNotificationHelper eventNotificationHelper;

    private Disposable subscription;

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, EventScheduleService.class);
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context) {
        Intent intent = getServiceIntent(context);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static void reschedule(ScheduleHelper scheduleHelper, Context context) {
        LocalDate today = LocalDate.now();
        DateTime midnight = DateUtils.nextDayMidnight(today);
        scheduleHelper.installExactAlarm(getPendingIntent(0, context), midnight.getMillis());
    }

    private static boolean isInTheFuture(@NonNull MasterEvent event) {
        return event.getMasterEventId() != null
                && event.getNotifyDateTime() != null
                && event.getNotifyDateTime().getMillis() >= DateTime.now().getMillis();
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
        reschedule(scheduleHelper, this);
        subscribeOnUpdates();
    }

    private void subscribeOnUpdates() {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(
                calendarInteractor.getAll(
                        GetEventsRequest.builder()
                                .date(LocalDate.now())
                                .filter(GetEventsFilter.builder()
                                        .eventTypes(Arrays.asList(EventType.values()))
                                        .build())
                                .child(Child.NULL)
                                .getScheduled(true)
                                .build())
                        .map(GetEventsResponse::getEvents)
                        .flatMap(Observable::fromIterable)
                        .filter(EventScheduleService::isInTheFuture)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResult, this::onUnexpectedError));
    }

    private void handleResult(@NonNull MasterEvent event) {
        logger.debug("schedule event: " + event);
        int requestCode = eventNotificationHelper.getNotificationId(event);
        PendingIntent pendingIntent = AlarmEventService.getPendingIntent(requestCode, this, event);
        long time = event.getNotifyDateTime().getMillis();
        scheduleHelper.installExactAlarm(pendingIntent, time);
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
    }
}
