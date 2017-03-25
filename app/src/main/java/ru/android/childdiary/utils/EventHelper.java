package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;

public class EventHelper {
    public static boolean canBeDone(@NonNull MasterEvent event) {
        return canBeDone(event.getEventType());
    }

    public static boolean canBeDone(@Nullable EventType eventType) {
        return eventType == EventType.OTHER;
    }

    public static boolean isDone(@NonNull MasterEvent event) {
        return event.getIsDone() != null && event.getIsDone();
    }

    public static boolean isExpired(@NonNull MasterEvent event) {
        return TimeUtils.isBeforeOrEqualNow(event.getDateTime());
    }

    public static boolean isTimerStarted(@Nullable SleepEvent event) {
        return event != null && event.getIsTimerStarted() != null && event.getIsTimerStarted();
    }

    public static String getTitle(Context context, @NonNull MasterEvent event) {
        return StringUtils.eventType(context, event.getEventType());
    }

    public static String getDescription(Context context, @NonNull MasterEvent event) {
        return event.getDescription();
    }
}
