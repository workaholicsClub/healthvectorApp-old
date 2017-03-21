package ru.android.childdiary.services;

import android.os.Binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

class TimerServiceBinder extends Binder implements TimerServiceListener {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @Getter
    private TimerService service;
    private List<TimerServiceListener> listeners = new ArrayList<>();

    public TimerServiceBinder(TimerService service) {
        this.service = service;
    }

    public void subscribe(TimerServiceListener listener) {
        listeners.add(listener);
        listener.onTimerTick();
    }

    public void unsubscribe(TimerServiceListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onTimerTick() {
        for (TimerServiceListener listener : listeners) {
            listener.onTimerTick();
        }
    }
}
