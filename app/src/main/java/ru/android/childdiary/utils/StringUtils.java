package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;

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
    public static String sex(Context context, Sex sex) {
        return sex(context, sex, null);
    }

    @Nullable
    public static String sex(Context context, Sex sex, @Nullable String defaultValue) {
        if (sex == null) {
            return defaultValue;
        }
        switch (sex) {
            case MALE:
                return context.getString(R.string.male);
            case FEMALE:
                return context.getString(R.string.female);
            default:
                return null;
        }
    }

    @Nullable
    public static String date(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        return date(localDate, dateTimeFormatter, null);
    }

    @Nullable
    public static String date(LocalDate localDate, DateTimeFormatter dateTimeFormatter, @Nullable String defaultValue) {
        return localDate == null ? defaultValue : localDate.toString(dateTimeFormatter);
    }

    @Nullable
    public static String time(LocalTime localTime, DateTimeFormatter dateTimeFormatter) {
        return time(localTime, dateTimeFormatter, null);
    }

    @Nullable
    public static String time(LocalTime localTime, DateTimeFormatter dateTimeFormatter, @Nullable String defaultValue) {
        return localTime == null ? defaultValue : localTime.toString(dateTimeFormatter);
    }

    public static String toString(@Nullable List<Child> childList) {
        return childList == null
                ? "null"
                : Arrays.toString(childList.toArray(new Child[childList.size()]));
    }
}
