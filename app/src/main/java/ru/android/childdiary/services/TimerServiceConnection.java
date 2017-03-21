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
            logger.warn("connection is already stopped");
            stop();
            return;
        }

        binder = (TimerServiceBinder) service;

        if (binder == null) {
            logger.error("binder is null");
            stop();
            return;
        }

        subscribeListener();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        logger.warn("onServiceDisconnected");
        stop();
    }

    public void start() {
        if (isStopped) {
            logger.warn("connection is already stopped");
            stop();
            return;
        }

        bindService();
    }

    public void stop() {
        unsubscribeListener();
        unbindService();
        isStopped = true;
    }

    private void bindService() {
        logger.debug("bind service");
        Intent serviceIntent = new Intent(context, TimerService.class);
        context.bindService(serviceIntent, this, 0);
        isBound = true;
    }

    private void unbindService() {
        if (isBound) {
            logger.debug("unbind service");
            context.unbindService(this);
            isBound = false;
        }
    }

    private void subscribeListener() {
        if (listener != null) {
            logger.debug("subscribe listener");
            binder.subscribe(listener);
        }
    }

    private void unsubscribeListener() {
        if (binder != null) {
            logger.debug("unsubscribe listener");
            binder.unsubscribe(listener);
            binder = null;
        }
        listener = null;
    }

    public boolean isEstablished() {
        return binder != null;
    }

    @Nullable
    public TimerService getService() {
        return binder == null ? null : binder.getService();
    }
}
