package ru.android.healthvector.receivers.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseReceiver extends BroadcastReceiver {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    @CallSuper
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("onReceive: " + intent);
    }
}
