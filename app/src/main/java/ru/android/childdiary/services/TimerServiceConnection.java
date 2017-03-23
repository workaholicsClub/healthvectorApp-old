package ru.android.childdiary.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerServiceConnection implements ServiceConnection {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final Context context;
    private TimerServiceListener listener;

    private TimerServiceBinder binder;
    private boolean isBound, isStopped;

    public TimerServiceConnection(Context context, @Nullable TimerServiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onServiceConnected(ComponentName name, final IBinder service) {
        logger.debug("onServiceConnected");

        if (isStopped) {
            logger.warn("connection is already closed");
            return;
        }

        binder = (TimerServiceBinder) service;
        if (binder == null) {
            logger.error("binder is null");
            return;
        }

        if (listener != null) {
            binder.subscribe(listener);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        logger.debug("onServiceDisconnected");
        close();
    }

    public void open() {
        if (isStopped) {
            logger.warn("connection is already closed");
            return;
        }

        Intent serviceIntent = new Intent(context, TimerService.class);
        context.bindService(serviceIntent, this, 0);
        isBound = true;
    }

    public void close() {
        if (binder != null) {
            binder.unsubscribe(listener);
            binder = null;
        }

        if (isBound) {
            context.unbindService(this);
            isBound = false;
        }

        listener = null;
        isStopped = true;
    }

    public boolean isEstablished() {
        return binder != null;
    }

    @Nullable
    public TimerService getService() {
        return binder == null ? null : binder.getService();
    }
}
