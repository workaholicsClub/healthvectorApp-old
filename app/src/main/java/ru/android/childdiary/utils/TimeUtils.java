package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;

public class TimeUtils {
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int MINUTES_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR;

    @Nullable
    public static String age(Context context, Child child) {
        LocalDate birthDate = child.getBirthDate();

        if (birthDate == null) {
            return null;
        }

        LocalDate now = LocalDate.now();

        if (birthDate.isAfter(now)) {
            return null;
        }

        Months period = Months.monthsBetween(birthDate, now);
        int years = period.getMonths() / 12;
        int months = period.getMonths() % 12;

        if (years == 0 && months == 0) {
            return context.getString(R.string.newborn);
        }

        String yearsString = context.getResources().getQuantityString(R.plurals.numberOfYears, years, years);
        String monthsString = context.getResources().getQuantityString(R.plurals.numberOfMonths, months, months);

        if (years == 0) {
            return monthsString;
        }

        if (months == 0) {
            return yearsString;
        }

        return context.getString(R.string.years_and_months, yearsString, monthsString);
    }

    public static Time.TimeBuilder splitMinutes(int minutes) {
        Time.TimeBuilder timeBuilder = Time.builder();

        if (minutes <= 0) {
            return timeBuilder;
        }

        int days = minutes / MINUTES_IN_DAY;
        timeBuilder.days(days);

        minutes -= days * MINUTES_IN_DAY;
        int hours = minutes / MINUTES_IN_HOUR;
        timeBuilder.hours(hours);

        minutes -= hours * MINUTES_IN_HOUR;
        timeBuilder.minutes(minutes);

        return timeBuilder;
    }

    private static String time(Context context, @NonNull Time time) {
        StringBuilder result = new StringBuilder();

        if (time.getDays() > 0) {
            if (time.isShortDays()) {
                result.append(context.getString(R.string.days_short, time.getDays()));
            } else {
                result.append(context.getResources().getQuantityString(R.plurals.numberOfDays,
                        time.getDays(), time.getDays()));
            }
        }

        if (time.getHours() > 0) {
            if (result.length() > 0) {
                result.append(' ');
            }
            if (time.isShortHours()) {
                result.append(context.getString(R.string.hours_short, time.getHours()));
            } else {
                result.append(context.getResources().getQuantityString(R.plurals.numberOfHours,
                        time.getHours(), time.getHours()));
            }
        }

        if (time.getMinutes() > 0) {
            if (result.length() > 0) {
                result.append(' ');
            }
            if (time.isShortMinutes()) {
                result.append(context.getString(R.string.minutes_short, time.getMinutes()));
            } else {
                result.append(context.getResources().getQuantityString(R.plurals.numberOfMinutes,
                        time.getMinutes(), time.getMinutes()));
            }
        }

        return result.toString();
    }

    public static String notifyTime(Context context, @Nullable Integer minutes) {
        if (minutes == null || minutes <= 0) {
            return context.getString(R.string.no_notification);
        }

        Time.TimeBuilder timeBuilder = splitMinutes(minutes);
        timeBuilder.shortDays(true);
        timeBuilder.shortHours(true);
        timeBuilder.shortMinutes(true);
        String time = time(context, timeBuilder.build());
        return context.getString(R.string.notify_time_text, time);
    }

    @Nullable
    public static String sleepTime(Context context, @Nullable DateTime start, @Nullable DateTime finish) {
        if (start == null || finish == null || start.isAfter(finish)) {
            return null;
        }

        Minutes minutes = Minutes.minutesBetween(start, finish);
        Time.TimeBuilder timeBuilder = splitMinutes(minutes.getMinutes());
        timeBuilder.shortDays(false);
        timeBuilder.shortHours(false);
        timeBuilder.shortMinutes(false);
        String time = time(context, timeBuilder.build());
        return time;
    }

    @Nullable
    public static String duration(Context context, @Nullable Integer minutes) {
        if (minutes == null || minutes <= 0) {
            return context.getString(R.string.no_notification);
        }

        Time.TimeBuilder timeBuilder = splitMinutes(minutes);
        timeBuilder.shortDays(true);
        timeBuilder.shortHours(true);
        timeBuilder.shortMinutes(true);
        String time = time(context, timeBuilder.build());
        return time;
    }

    @Value
    @Builder
    public static class Time {
        int days;
        int hours;
        int minutes;
        boolean shortDays;
        boolean shortHours;
        boolean shortMinutes;
    }
}
