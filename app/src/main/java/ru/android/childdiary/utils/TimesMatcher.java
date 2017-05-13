package ru.android.childdiary.utils;

import android.support.annotation.NonNull;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimesMatcher {
    private static final int INTERVAL = 15;

    public static List<LocalTime> match(@NonNull LocalTime startTime, @NonNull LocalTime finishTime, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Expected non-negative value");
        }

        if (n == 0) {
            return Collections.emptyList();
        }

        startTime = roundUpTime(startTime);
        finishTime = roundDownTime(finishTime);

        if (finishTime.isBefore(startTime)) {
            finishTime = startTime;
        }

        if (n == 1) {
            return Collections.singletonList(startTime);
        }

        int minutesBetweenStartAndFinish = Minutes.minutesBetween(startTime, finishTime).getMinutes();

        float minutesInInterval = minutesBetweenStartAndFinish / (float) (n - 1);

        List<LocalTime> times = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            int minutes = Math.round(minutesInInterval * i);
            LocalTime time = roundTime(startTime.plusMinutes(minutes));
            times.add(time);
        }

        return times;
    }

    private static LocalTime roundTime(LocalTime time) {
        int min = time.getMinuteOfHour();
        int mod = min % INTERVAL;
        if (mod > INTERVAL / 2) {
            min = INTERVAL - mod;
        } else {
            min = 0 - mod;
        }
        return time.plusMinutes(min);
    }

    private static LocalTime roundUpTime(LocalTime time) {
        int min = time.getMinuteOfHour();
        int mod = min % INTERVAL;
        if (mod == 0) {
            return time;
        }
        min = INTERVAL - mod;
        return time.plusMinutes(min);
    }

    private static LocalTime roundDownTime(LocalTime time) {
        int min = time.getMinuteOfHour();
        int mod = min % INTERVAL;
        if (mod == 0) {
            return time;
        }
        min = 0 - mod;
        return time.plusMinutes(min);
    }
}
