package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;

public class TimeUtils {
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int MINUTES_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR;

    private static final DateTimeComparator COMPARE_ONLY_DATE = DateTimeComparator.getDateOnlyInstance();
    private static final DateTimeComparator COMPARE_ONLY_TIME = DateTimeComparator.getTimeOnlyInstance();

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
    private static String duration(Context context, @Nullable Integer minutes, boolean allShort) {
        if (minutes == null || minutes <= 0) {
            return null;
        }

        Time.TimeBuilder timeBuilder = splitMinutes(minutes);
        timeBuilder.shortDays(allShort);
        timeBuilder.shortHours(allShort);
        timeBuilder.shortMinutes(allShort);
        String time = time(context, timeBuilder.build());
        return time;
    }

    @Nullable
    public static String durationShort(Context context, @Nullable Integer minutes) {
        return duration(context, minutes, true);
    }

    @Nullable
    public static String durationLong(Context context, @Nullable Integer minutes) {
        return duration(context, minutes, false);
    }

    @Nullable
    private static String duration(Context context, @Nullable DateTime start, @Nullable DateTime finish, boolean allShort) {
        Integer minutes = durationInMinutes(start, finish);
        if (minutes == null) {
            return null;
        }

        return duration(context, minutes, allShort);
    }

    @Nullable
    public static String durationShort(Context context, @Nullable DateTime start, @Nullable DateTime finish) {
        return duration(context, start, finish, true);
    }

    @Nullable
    public static String durationLong(Context context, @Nullable DateTime start, @Nullable DateTime finish) {
        return duration(context, start, finish, false);
    }

    @Nullable
    public static String timerString(Context context, @Nullable DateTime start, @Nullable DateTime finish) {
        if (start == null || finish == null || start.isAfter(finish)) {
            return context.getString(R.string.duration_format);
        }

        Duration duration = new Duration(start, finish);
        Period period = duration.toPeriod();
        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSeparator(context.getString(R.string.duration_separator))
                .appendMinutes()
                .appendSeparator(context.getString(R.string.duration_separator))
                .appendSeconds()
                .toFormatter();
        String result = periodFormatter.print(period);
        return result;
    }

    @Nullable
    public static Integer durationInMinutes(@Nullable DateTime start, @Nullable DateTime finish) {
        if (start == null || finish == null || start.isAfter(finish)) {
            return null;
        }

        start = start.withSecondOfMinute(0).withMillisOfSecond(0);
        finish = finish.withSecondOfMinute(0).withMillisOfSecond(0);
        Minutes minutes = Minutes.minutesBetween(start, finish);
        return minutes.getMinutes();
    }

    public static boolean isStartTimeLessThanFinishTime(@Nullable DateTime start, @Nullable DateTime finish) {
        if (start == null || finish == null) {
            return false;
        }
        boolean sameDate = COMPARE_ONLY_DATE.compare(start, finish) == 0;
        boolean isStartTimeAfterFinishTime = COMPARE_ONLY_TIME.compare(start, finish) > 0;
        return sameDate && isStartTimeAfterFinishTime;
    }

    @Nullable
    public static String numberOfTimes(Context context, @Nullable Integer number) {
        if (number == null) {
            return null;
        }
        return context.getResources().getQuantityString(R.plurals.numberOfTimesInADay, number, number);
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
