package ru.android.childdiary.receivers.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseReceiver extends BroadcastReceiver {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @CallSuper
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("onReceive: " + intent);
    }
}
