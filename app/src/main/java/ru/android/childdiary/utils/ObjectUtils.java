package ru.android.childdiary.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public class ObjectUtils {
    public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }

    public static boolean contentEquals(@Nullable String s1, @Nullable String s2) {
        return isEmpty(s1) ? isEmpty(s2) : s1.equals(s2);
    }

    public static boolean isPositive(Integer number) {
        return number != null && number > 0;
    }

    public static boolean isPositive(Double number) {
        return number != null && number > 0;
    }

    public static boolean isEmpty(@NonNull Child child) {
        return contentEquals(child, Child.NULL);
    }

    public static boolean contentEquals(@NonNull Child child1, @NonNull Child child2) {
        return contentEquals(child1.getName(), child2.getName())
                && equals(child1.getBirthDate(), child2.getBirthDate())
                && equals(child1.getBirthTime(), child2.getBirthTime())
                && child1.getSex() == child2.getSex()
                && contentEquals(child1.getImageFileName(), child2.getImageFileName())
                && equals(child1.getBirthHeight(), child2.getBirthHeight())
                && equals(child1.getBirthWeight(), child2.getBirthWeight());
    }

    private static boolean equals(@Nullable DateTime dateTime1, @Nullable DateTime dateTime2) {
        if (dateTime1 == null) {
            return dateTime2 == null;
        }
        if (dateTime2 == null) {
            return false;
        }
        dateTime1 = dateTime1.withSecondOfMinute(0).withMillisOfSecond(0);
        dateTime2 = dateTime2.withSecondOfMinute(0).withMillisOfSecond(0);
        return dateTime1.isEqual(dateTime2);
    }

    public static boolean contentEquals(@NonNull MasterEvent event1, @NonNull MasterEvent event2) {
        return equals(event1.getDateTime(), event2.getDateTime())
                && equals(event1.getNotifyTimeInMinutes(), event2.getNotifyTimeInMinutes())
                && contentEquals(event1.getNote(), event2.getNote());
    }

    public static boolean contentEquals(@NonNull DiaperEvent event1, @NonNull DiaperEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && event1.getDiaperState() == event2.getDiaperState();
    }

    public static boolean contentEquals(@NonNull FeedEvent event1, @NonNull FeedEvent event2) {
        if (!contentEquals(event1.getMasterEvent(), event2.getMasterEvent())) {
            return false;
        }
        if (event1.getFeedType() != event2.getFeedType()) {
            return false;
        }
        switch (event1.getFeedType()) {
            case BREAST_MILK:
                return event1.getBreast() == event2.getBreast()
                        && equals(event1.getLeftDurationInMinutes(), event2.getLeftDurationInMinutes())
                        && equals(event1.getRightDurationInMinutes(), event2.getRightDurationInMinutes());
            case PUMPED_MILK:
                return equals(event1.getAmountMl(), event2.getAmountMl());
            case MILK_FORMULA:
                return equals(event1.getAmountMl(), event2.getAmountMl());
            case FOOD:
                return equals(event1.getAmount(), event2.getAmount())
                        && equals(event1.getFood(), event2.getFood())
                        && equals(event1.getFoodMeasure(), event2.getFoodMeasure());
        }
        return false;
    }

    public static boolean contentEquals(@NonNull OtherEvent event1, @NonNull OtherEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && contentEquals(event1.getName(), event2.getName())
                && equals(event1.getFinishDateTime(), event2.getFinishDateTime());
    }

    public static boolean contentEquals(@NonNull PumpEvent event1, @NonNull PumpEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && event1.getBreast() == event2.getBreast()
                && equals(event1.getLeftAmountMl(), event2.getLeftAmountMl())
                && equals(event1.getRightAmountMl(), event2.getRightAmountMl());
    }

    public static boolean contentEquals(@NonNull SleepEvent event1, @NonNull SleepEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && equals(event1.getFinishDateTime(), event2.getFinishDateTime());
    }
}
