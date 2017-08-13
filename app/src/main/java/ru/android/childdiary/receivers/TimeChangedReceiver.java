package ru.android.childdiary.receivers;

import android.content.Context;
import android.content.Intent;

import ru.android.childdiary.receivers.core.BaseReceiver;
import ru.android.childdiary.services.core.ScheduleHelper;

public class TimeChangedReceiver extends BaseReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ScheduleHelper.scheduleAllRepeatingTasks(context);
    }
}
