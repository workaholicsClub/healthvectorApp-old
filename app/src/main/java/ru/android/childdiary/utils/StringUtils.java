package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormatter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;

public class StringUtils {
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

    @Nullable
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

    @Nullable
    public static String print(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        return print(localDate, dateTimeFormatter, null);
    }

    @Nullable
    public static String print(LocalDate localDate, DateTimeFormatter dateTimeFormatter, String defaultValue) {
        return localDate == null ? defaultValue : localDate.toString(dateTimeFormatter);
    }

    @Nullable
    public static String print(LocalTime localTime, DateTimeFormatter dateTimeFormatter) {
        return print(localTime, dateTimeFormatter, null);
    }

    @Nullable
    public static String print(LocalTime localTime, DateTimeFormatter dateTimeFormatter, String defaultValue) {
        return localTime == null ? defaultValue : localTime.toString(dateTimeFormatter);
    }
}
