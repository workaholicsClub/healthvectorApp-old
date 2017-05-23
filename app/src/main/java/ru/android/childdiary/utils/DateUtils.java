package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ru.android.childdiary.R;

public class DateUtils {
    private static String[] nominativeMonthNames, genitiveMonthNames;

    @Nullable
    public static String date(Context context, @Nullable DateTime dateTime) {
        return date(context, dateTime, null);
    }

    @Nullable
    public static String date(Context context, @Nullable DateTime dateTime, @Nullable String defaultValue) {
        DateTimeFormatter formatter = getDateFormatter(context);
        return dateTime == null ? defaultValue : dateTime.toLocalDate().toString(formatter);
    }

    @Nullable
    public static String date(Context context, @Nullable LocalDate localDate) {
        return date(context, localDate, null);
    }

    @Nullable
    public static String date(Context context, @Nullable LocalDate localDate, @Nullable String defaultValue) {
        DateTimeFormatter formatter = getDateFormatter(context);
        return localDate == null ? defaultValue : localDate.toString(formatter);
    }

    @Nullable
    public static String time(Context context, @Nullable DateTime dateTime) {
        return time(context, dateTime, null);
    }

    @Nullable
    public static String time(Context context, @Nullable DateTime dateTime, @Nullable String defaultValue) {
        DateTimeFormatter formatter = getTimeFormatter(context);
        return dateTime == null ? defaultValue : dateTime.toLocalTime().toString(formatter);
    }

    @Nullable
    public static String time(Context context, @Nullable LocalTime localTime) {
        return time(context, localTime, null);
    }

    @Nullable
    public static String time(Context context, @Nullable LocalTime localTime, @Nullable String defaultValue) {
        DateTimeFormatter formatter = getTimeFormatter(context);
        return localTime == null ? defaultValue : localTime.toString(formatter);
    }

    public static String monthNominativeName(Context context, int month) {
        int monthIndex = month - 1;
        if (nominativeMonthNames == null) {
            nominativeMonthNames = context.getResources().getStringArray(R.array.nominative_month_names);
        }
        return nominativeMonthNames[monthIndex];
    }

    public static String monthGenitiveName(Context context, int month) {
        int monthIndex = month - 1;
        if (genitiveMonthNames == null) {
            genitiveMonthNames = context.getResources().getStringArray(R.array.genitive_month_names);
        }
        return genitiveMonthNames[monthIndex];
    }

    private static DateTimeFormatter getDateFormatter(Context context) {
        String pattern = context.getString(R.string.date_format);
        return DateTimeFormat.forPattern(pattern);
    }

    private static DateTimeFormatter getTimeFormatter(Context context) {
        String pattern = context.getString(R.string.time_format);
        return DateTimeFormat.forPattern(pattern);
    }

    public static DateTime midnight(LocalDate date) {
        return date.toDateTime(LocalTime.MIDNIGHT);
    }

    public static DateTime nextDayMidnight(LocalDate date) {
        return date.plusDays(1).toDateTime(LocalTime.MIDNIGHT);
    }
}
