package ru.android.childdiary.utils;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;

public class StringUtils {
    public static String print(Context context, Sex sex) {
        if (sex == null) {
            return null;
        }
        String[] strings = context.getResources().getStringArray(R.array.sex_variants);
        switch (sex) {
            case MALE:
                return strings[1];
            case FEMALE:
                return strings[2];
            default:
                return null;
        }
    }

    public static String print(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        return print(localDate, dateTimeFormatter, null);
    }

    public static String print(LocalDate localDate, DateTimeFormatter dateTimeFormatter, String defaultValue) {
        return localDate == null ? defaultValue : localDate.toString(dateTimeFormatter);
    }

    public static String print(LocalTime localTime, DateTimeFormatter dateTimeFormatter) {
        return print(localTime, dateTimeFormatter, null);
    }

    public static String print(LocalTime localTime, DateTimeFormatter dateTimeFormatter, String defaultValue) {
        return localTime == null ? defaultValue : localTime.toString(dateTimeFormatter);
    }
}
