package ru.android.childdiary.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.android.childdiary.services.CloudService;

public class TimeChangedReceiver extends BroadcastReceiver {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("onReceive: " + intent);
        CloudService.installAlarm(context);
    }
}
