package ru.android.childdiary.utils;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

public class StringUtils {
    public static String print(String str) {
        return str == null ? "" : str;
    }

    public static String print(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        return localDate == null ? "" : localDate.toString(dateTimeFormatter);
    }

    public static String print(LocalTime localTime, DateTimeFormatter dateTimeFormatter) {
        return localTime == null ? "" : localTime.toString(dateTimeFormatter);
    }
}
