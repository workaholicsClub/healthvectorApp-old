package ru.android.childdiary.services;

import android.os.Binder;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;

class TimerServiceBinder extends Binder implements TimerServiceListener {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @Getter
    private final TimerService service;
    private final List<TimerServiceListener> listeners = new ArrayList<>();

    public TimerServiceBinder(TimerService service) {
        this.service = service;
    }

    public void subscribe(TimerServiceListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(TimerServiceListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onTimerTick(@NonNull SleepEvent event) {
        for (TimerServiceListener listener : listeners) {
            listener.onTimerTick(event);
        }
    }
}
