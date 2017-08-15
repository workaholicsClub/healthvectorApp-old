package ru.android.childdiary.data.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import ru.android.childdiary.services.CloudService;
import ru.android.childdiary.services.LinearGroupFinishedService;
import ru.android.childdiary.utils.strings.DateUtils;

public class ScheduleHelper {
    private final Context context;
    private final AlarmManager alarmManager;

    public ScheduleHelper(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleAll() {
        LocalDate today = LocalDate.now();
        DateTime midnight = DateUtils.nextDayMidnight(today);
        // schedule on 17:00 today or next day
        DateTime fiveOClock = today.toDateTime(new LocalTime(17, 0));
        if (fiveOClock.isBeforeNow()) {
            fiveOClock = fiveOClock.plusDays(1);
        }

        installRepeatingAlarm(CloudService.getPendingIntent(0, context), midnight.getMillis());
        installRepeatingAlarm(LinearGroupFinishedService.getPendingIntent(0, context), fiveOClock.getMillis());
    }

    private void installRepeatingAlarm(@NonNull PendingIntent pendingIntent, long millis) {
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void installAlarm(@NonNull PendingIntent pendingIntent, long millis) {
        alarmManager.cancel(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }
    }
}
