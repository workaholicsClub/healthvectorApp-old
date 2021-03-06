package ru.android.healthvector.utils.strings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.child.data.Child;

public class TimeUtils {
    public static final int MONTHS_IN_YEAR = 12;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int MINUTES_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR;

    @Nullable
    public static String age(Context context, @NonNull Child child) {
        LocalDate birthDate = child.getBirthDate();

        if (birthDate == null) {
            return null;
        }

        LocalDate now = LocalDate.now();

        if (birthDate.isAfter(now)) {
            return null;
        }

        int months = Months.monthsBetween(birthDate, now).getMonths();
        return age(context, months, false);
    }

    @Nullable
    public static Age getAge(@Nullable LocalDate birthDate, @Nullable LocalDate date) {
        if (birthDate == null || date == null || birthDate.isAfter(date)) {
            return null;
        }

        int months = Months.monthsBetween(birthDate, date).getMonths();
        return Age.builder().months(months).build();
    }

    public static double getMonths(@NonNull Child child) {
        return getMonths(child.getBirthDate(), LocalDate.now());
    }

    public static double getMonths(@NonNull LocalDate birthDate, @NonNull LocalDate date) {
        if (birthDate.isAfter(date)) {
            return 0;
        }
        int months = Months.monthsBetween(birthDate, date).getMonths();
        int days = Days.daysBetween(birthDate.plusMonths(months), date).getDays();
        double part = days / 30.0;
        if (part < 0) {
            part = 0;
        } else if (part > 1) {
            part = 1;
        }
        return months + part;
    }

    @Nullable
    public static String age(Context context, @Nullable Age age) {
        return age == null ? null : age(context, age.getMonths(), true);
    }

    public static String age(Context context, double months) {
        int intMonths = (int) months;
        String intMonthsStr = String.valueOf(intMonths);
        String doubleMonthsStr = DoubleUtils.submultipleUnitFormat(months);
        String ageStr = age(context, intMonths, true);
        return ageStr.replace(intMonthsStr, doubleMonthsStr);
    }

    private static String age(Context context, int months, boolean shortMonths) {
        int years = months / MONTHS_IN_YEAR;
        months = months % MONTHS_IN_YEAR;

        if (years == 0 && months == 0) {
            return context.getString(R.string.newborn);
        }

        String yearsString = context.getResources().getQuantityString(R.plurals.numberOfYears, years, years);
        String monthsString = shortMonths
                ? context.getString(R.string.months_short, months)
                : context.getResources().getQuantityString(R.plurals.numberOfMonths, months, months);

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
            if (result.length() > 0) {
                result.append(' ');
            }
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
        if (minutes == null || minutes < 0) {
            return context.getString(R.string.dont_notify);
        }

        if (minutes == 0) {
            return context.getString(R.string.in_the_moment);
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

    @Value
    @Builder
    public static class Age implements Serializable {
        int months;

        public int getYearsPart() {
            return months / MONTHS_IN_YEAR;
        }

        public int getMonthsPart() {
            return months % MONTHS_IN_YEAR;
        }
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
