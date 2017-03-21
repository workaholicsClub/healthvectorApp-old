package ru.android.childdiary.services;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;

public interface TimerServiceListener {
    void onTimerTick(@NonNull SleepEvent event);
}
