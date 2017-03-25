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

    public static boolean sameEvent(@Nullable MasterEvent event1, @Nullable MasterEvent event2) {
        return event1 != null && event2 != null
                && ObjectUtils.equals(event1.getMasterEventId(), event2.getMasterEventId());
    }

    public static boolean sameChild(@Nullable MasterEvent event1, @Nullable MasterEvent event2) {
        return event1 != null && event2 != null
                && event1.getChild() != null && event2.getChild() != null
                && ObjectUtils.equals(event1.getChild().getId(), event2.getChild().getId());
    }
}
