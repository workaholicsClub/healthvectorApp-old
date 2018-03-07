package ru.android.healthvector.services;

import android.support.annotation.NonNull;

import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;

public interface TimerServiceListener {
    void onTimerTick(@NonNull SleepEvent event);
}
