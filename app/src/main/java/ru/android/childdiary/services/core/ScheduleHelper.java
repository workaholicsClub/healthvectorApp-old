package ru.android.childdiary.services.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import ru.android.childdiary.services.CloudService;
import ru.android.childdiary.services.EventScheduleService;
import ru.android.childdiary.services.LinearGroupFinishedService;
import ru.android.childdiary.utils.strings.DateUtils;

public class ScheduleHelper {
    private final Context context;
    private final AlarmManager alarmManager;

    private ScheduleHelper(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Schedules alarms on application started and on time changed.
     *
     * @param context application context
     */
    public static void scheduleAllRepeatingTasks(Context context) {
        ScheduleHelper scheduleHelper = new ScheduleHelper(context);

        context.startService(new Intent(context, EventScheduleService.class));

        LocalDate today = LocalDate.now();
        DateTime midnight = DateUtils.nextDayMidnight(today);
        // schedule on 17:00 today or next day
        DateTime fiveOClock = today.toDateTime(new LocalTime(17, 0));
        if (fiveOClock.isBeforeNow()) {
            fiveOClock = fiveOClock.plusDays(1);
        }

        scheduleHelper.installAlarm(CloudService.getPendingIntent(context), midnight.getMillis());
        scheduleHelper.installAlarm(EventScheduleService.getPendingIntent(context), midnight.getMillis());
        scheduleHelper.installAlarm(LinearGroupFinishedService.getPendingIntent(context), fiveOClock.getMillis());
    }

    private void installAlarm(@NonNull PendingIntent pendingIntent, long millis) {
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
