package ru.android.childdiary.data.services;

import android.content.Context;
import android.support.annotation.NonNull;

import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.services.AccountService;
import ru.android.childdiary.services.EventScheduleService;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.services.UpdateDataService;

public class ServiceController {
    private final Context context;

    public ServiceController(Context context) {
        this.context = context;
    }

    public void onApplicationStart() {
        TimerService.startService(context, null, null);
        AccountService.startService(context);
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

    public void scheduleEvents() {
        EventScheduleService.startService(context);
    }
}
