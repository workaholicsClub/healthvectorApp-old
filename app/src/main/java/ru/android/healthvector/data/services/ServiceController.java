package ru.android.healthvector.data.services;

import android.content.Context;
import android.support.annotation.NonNull;

import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;
import ru.android.healthvector.services.AccountService;
import ru.android.healthvector.services.CloudService;
import ru.android.healthvector.services.EventScheduleService;
import ru.android.healthvector.services.LinearGroupFinishedService;
import ru.android.healthvector.services.TimerService;
import ru.android.healthvector.services.UpdateDataService;

public class ServiceController {
    private final Context context;
    private final ScheduleHelper scheduleHelper;

    public ServiceController(Context context, ScheduleHelper scheduleHelper) {
        this.context = context;
        this.scheduleHelper = scheduleHelper;
    }

    public void onApplicationStart() {
        TimerService.startService(context, null, null);
        AccountService.startService(context);

        onTimeChanged();
    }

    public void onTimeChanged() {
        EventScheduleService.startService(context);
        CloudService.reschedule(scheduleHelper, context);
        LinearGroupFinishedService.reschedule(scheduleHelper, context);
    }

    public void updateData() {
        UpdateDataService.startService(context);
    }

    public void stopSleepEventTimer(@NonNull SleepEvent event) {
        TimerService.startService(context, TimerService.ACTION_STOP_SLEEP_EVENT_TIMER, event);
    }

    public void resubscribeTimer() {
        TimerService.startService(context, TimerService.ACTION_RESUBSCRIBE_TIMER, null);
    }
}
