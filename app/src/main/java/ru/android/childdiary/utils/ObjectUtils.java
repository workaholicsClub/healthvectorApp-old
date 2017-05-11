package ru.android.childdiary.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

public class ObjectUtils {
    public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static boolean contentEquals(@Nullable String s1, @Nullable String s2) {
        return TextUtils.isEmpty(s1) ? TextUtils.isEmpty(s2) : s1.equals(s2);
    }

    public static boolean isPositive(@Nullable Integer number) {
        return number != null && number > 0;
    }

    public static boolean isPositive(@Nullable Double number) {
        return number != null && number > 0;
    }

    public static boolean isTrue(@Nullable Boolean value) {
        return value != null && value;
    }

    public static boolean isEmpty(@Nullable ContentObject object) {
        return object == null || object.isContentEmpty();
    }

    public static <T> boolean contentEquals(@Nullable ContentObject<T> object1, @Nullable ContentObject<T> object2) {
        //noinspection unchecked
        return object1 == null
                ? object2 == null
                : object2 != null && object1.isContentEqual((T) object2);
    }

    private static boolean equalsToMinutes(@Nullable DateTime dateTime1, @Nullable DateTime dateTime2) {
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

    private static boolean contentEquals(@NonNull MasterEvent event1, @NonNull MasterEvent event2) {
        return equalsToMinutes(event1.getDateTime(), event2.getDateTime())
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
        if (event1.getFeedType() == null) {
            return true;
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
                && equalsToMinutes(event1.getFinishDateTime(), event2.getFinishDateTime());
    }

    public static boolean contentEquals(@NonNull PumpEvent event1, @NonNull PumpEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && event1.getBreast() == event2.getBreast()
                && equals(event1.getLeftAmountMl(), event2.getLeftAmountMl())
                && equals(event1.getRightAmountMl(), event2.getRightAmountMl());
    }

    public static boolean contentEquals(@NonNull SleepEvent event1, @NonNull SleepEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && equalsToMinutes(event1.getFinishDateTime(), event2.getFinishDateTime());
    }

    public static boolean contentEquals(@NonNull DoctorVisitEvent event1, @NonNull DoctorVisitEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && equals(event1.getDoctor(), event2.getDoctor())
                && contentEquals(event1.getName(), event2.getName())
                && equals(event1.getDurationInMinutes(), event2.getDurationInMinutes())
                && contentEquals(event1.getImageFileName(), event2.getImageFileName());
    }

    public static boolean contentEquals(@NonNull MedicineTakingEvent event1, @NonNull MedicineTakingEvent event2) {
        return contentEquals(event1.getMasterEvent(), event2.getMasterEvent())
                && equals(event1.getMedicine(), event2.getMedicine())
                && equals(event1.getAmount(), event2.getAmount())
                && equals(event1.getMedicineMeasure(), event2.getMedicineMeasure())
                && contentEquals(event1.getImageFileName(), event2.getImageFileName());
    }

    // TODO EXERCISE

    public static boolean contentEquals(@NonNull DoctorVisit doctorVisit1, @NonNull DoctorVisit doctorVisit2) {
        return equals(doctorVisit1.getDoctor(), doctorVisit2.getDoctor())
                && contentEquals(doctorVisit1.getRepeatParameters(), doctorVisit2.getRepeatParameters())
                && contentEquals(doctorVisit1.getName(), doctorVisit2.getName())
                && equals(doctorVisit1.getDurationInMinutes(), doctorVisit2.getDurationInMinutes())
                && equalsToMinutes(doctorVisit1.getDateTime(), doctorVisit2.getDateTime())
                && equalsToMinutes(doctorVisit1.getFinishDateTime(), doctorVisit2.getFinishDateTime())
                && isTrue(doctorVisit1.getExported()) == isTrue(doctorVisit2.getExported())
                && equals(doctorVisit1.getNotifyTimeInMinutes(), doctorVisit2.getNotifyTimeInMinutes())
                && contentEquals(doctorVisit1.getNote(), doctorVisit2.getNote())
                && contentEquals(doctorVisit1.getImageFileName(), doctorVisit2.getImageFileName());
    }

    public static boolean contentEquals(@NonNull MedicineTaking medicineTaking1, @NonNull MedicineTaking medicineTaking2) {
        return equals(medicineTaking1.getMedicine(), medicineTaking2.getMedicine())
                && equals(medicineTaking1.getAmount(), medicineTaking2.getAmount())
                && equals(medicineTaking1.getMedicineMeasure(), medicineTaking2.getMedicineMeasure())
                && contentEquals(medicineTaking1.getRepeatParameters(), medicineTaking2.getRepeatParameters())
                && equalsToMinutes(medicineTaking1.getDateTime(), medicineTaking2.getDateTime())
                && equalsToMinutes(medicineTaking1.getFinishDateTime(), medicineTaking2.getFinishDateTime())
                && isTrue(medicineTaking1.getExported()) == isTrue(medicineTaking2.getExported())
                && equals(medicineTaking1.getNotifyTimeInMinutes(), medicineTaking2.getNotifyTimeInMinutes())
                && contentEquals(medicineTaking1.getNote(), medicineTaking2.getNote())
                && contentEquals(medicineTaking1.getImageFileName(), medicineTaking2.getImageFileName());
    }
}
