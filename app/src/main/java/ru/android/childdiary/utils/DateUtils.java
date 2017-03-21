package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ru.android.childdiary.R;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy");

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    private static String[] nominativeMonthNames, genitiveMonthNames;

    @Nullable
    public static String date(@Nullable LocalDate localDate) {
        return date(localDate, null);
    }

    @Nullable
    public static String date(@Nullable LocalDate localDate, @Nullable String defaultValue) {
        return localDate == null ? defaultValue : localDate.toString(DATE_FORMATTER);
    }

    @Nullable
    public static String time(@Nullable LocalTime localTime) {
        return time(localTime, null);
    }

    @Nullable
    public static String time(@Nullable LocalTime localTime, @Nullable String defaultValue) {
        return localTime == null ? defaultValue : localTime.toString(TIME_FORMATTER);
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
}
