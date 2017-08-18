package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.services.ScheduleHelper;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.services.core.BaseService;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.notifications.LinearGroupNotificationHelper;

public class LinearGroupFinishedService extends BaseService {
    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    ScheduleHelper scheduleHelper;

    @Inject
    LinearGroupNotificationHelper linearGroupNotificationHelper;

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, LinearGroupFinishedService.class);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context) {
        Intent intent = getServiceIntent(context);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void reschedule(ScheduleHelper scheduleHelper, Context context) {
        LocalDate today = LocalDate.now();
        DateTime fiveOClock = today.toDateTime(new LocalTime(17, 0));
        if (fiveOClock.isBeforeNow()) {
            fiveOClock = fiveOClock.plusDays(1);
        }
        scheduleHelper.installExactAlarm(getPendingIntent(0, context), fiveOClock.getMillis());
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        reschedule(scheduleHelper, this);
        unsubscribeOnDestroy(calendarInteractor.getFinishedLinearGroupEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::onUnexpectedError));
    }

    private void handleResult(@NonNull List<MasterEvent> events) {
        for (MasterEvent event : events) {
            logger.debug("finished event: " + event);
            EventNotification eventNotification = calendarInteractor.getNotificationSettings(event.getEventType()).blockingFirst(); // TODO
            linearGroupNotificationHelper.showEventNotification(event, eventNotification);
        }
        stopSelf();
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
        stopSelf();
    }
}
