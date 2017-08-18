package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.inject.Inject;

import ru.android.childdiary.data.services.ScheduleHelper;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.services.core.BaseService;

public class LinearGroupFinishedService extends BaseService {
    // TODO interactor

    @Inject
    ScheduleHelper scheduleHelper;

    // TODO notification helper

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
        Toast.makeText(this, "linear group finished", Toast.LENGTH_LONG).show();
        stopSelf();
    }
}
