package ru.android.childdiary.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootCompletedReceiver extends BroadcastReceiver {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("onReceive: " + intent);
    }
}
