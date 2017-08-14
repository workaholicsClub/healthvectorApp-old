package ru.android.childdiary.receivers;

import android.content.Context;
import android.content.Intent;

import org.joda.time.LocalTime;

import javax.inject.Inject;

import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.data.services.ServiceController;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.receivers.core.BaseReceiver;
import ru.android.childdiary.data.services.ScheduleHelper;

public class TimeChangedReceiver extends BaseReceiver {
    @Inject
    ServiceController serviceController;

    @Inject
    ScheduleHelper scheduleHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ApplicationComponent applicationComponent = ChildDiaryApplication.getApplicationComponent();
        applicationComponent.inject(this);
        serviceController.scheduleEvents(LocalTime.now());
        scheduleHelper.scheduleAll();
    }
}
