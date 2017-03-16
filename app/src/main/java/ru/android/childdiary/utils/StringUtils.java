package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.Arrays;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public class StringUtils {
    private static final int MINUTES_IN_HOUR = 60;
    private static final int MINUTES_IN_DAY = 24 * MINUTES_IN_HOUR;

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

    public static String childList(@Nullable List<Child> list) {
        return list == null
                ? "null"
                : Arrays.toString(list.toArray(new Child[list.size()]));
    }

    public static String eventsList(@Nullable List<MasterEvent> list) {
        return list == null
                ? "null"
                : Arrays.toString(list.toArray(new MasterEvent[list.size()]));
    }

    @Nullable
    public static String breast(Context context, @Nullable Breast breast) {
        if (breast == null) {
            return null;
        }
        switch (breast) {
            case LEFT:
                return context.getString(R.string.breast_left);
            case RIGHT:
                return context.getString(R.string.breast_right);
            default:
                return null;
        }
    }

    @Nullable
    public static String diaperState(Context context, @Nullable DiaperState diaperState) {
        if (diaperState == null) {
            return null;
        }
        switch (diaperState) {
            case DIRTY:
                return context.getString(R.string.diaper_state_dirty);
            case DRY:
                return context.getString(R.string.diaper_state_dry);
            case MIXED:
                return context.getString(R.string.diaper_state_mixed);
            case WET:
                return context.getString(R.string.diaper_state_wet);
            default:
                return null;
        }
    }

    @Nullable
    public static String eventType(Context context, @Nullable EventType eventType) {
        if (eventType == null) {
            return null;
        }
        switch (eventType) {
            case DIAPER:
                return context.getString(R.string.event_diaper);
            case SLEEP:
                return context.getString(R.string.event_sleep);
            case FEED:
                return context.getString(R.string.event_feed);
            case PUMP:
                return context.getString(R.string.event_pump);
            case OTHER:
                return context.getString(R.string.event_other);
            default:
                return null;
        }
    }

    @Nullable
    public static String feedType(Context context, @Nullable FeedType feedType) {
        if (feedType == null) {
            return null;
        }
        switch (feedType) {
            case BREAST_MILK:
                return context.getString(R.string.feed_type_breast_milk);
            case PUMPED_MILK:
                return context.getString(R.string.feed_type_pumped_milk);
            case MILK_FORMULA:
                return context.getString(R.string.feed_type_milk_formula);
            case FOOD:
                return context.getString(R.string.feed_type_food);
            default:
                return null;
        }
    }

    @Nullable
    private static String time(Context context, @Nullable Integer minutes) {
        if (minutes == null || minutes <= 0) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        int days = minutes / MINUTES_IN_DAY;
        if (days > 0) {
            result.append(context.getResources().getQuantityString(R.plurals.numberOfDays, days, days));
        }

        minutes -= days * MINUTES_IN_DAY;
        int hours = minutes / MINUTES_IN_HOUR;
        if (hours > 0) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(context.getResources().getQuantityString(R.plurals.numberOfHours, hours, hours));
        }

        minutes -= hours * MINUTES_IN_HOUR;
        if (minutes > 0) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(context.getResources().getQuantityString(R.plurals.numberOfMinutes, minutes, minutes));
        }

        return result.toString();
    }

    @Nullable
    public static String notifyTime(Context context, @Nullable Integer minutes) {
        String time = time(context, minutes);
        return time == null ? null : context.getString(R.string.notify_time_text, time);
    }

    @Nullable
    public static String duration(Context context, @Nullable Integer minutes) {
        return time(context, minutes);
    }
}
