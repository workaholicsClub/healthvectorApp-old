package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasureValue;

public class StringUtils {
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
            case DOCTOR_VISIT:
                return context.getString(R.string.event_doctor_visit);
            case MEDICINE_TAKING:
                return context.getString(R.string.event_medicine_taking);
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
    public static String medicineMeasureValue(Context context,
                                              @Nullable MedicineMeasureValue medicineMeasureValue) {
        return medicineMeasureValue == null
                ? null
                : medicineMeasureValue(context, medicineMeasureValue.getAmount(), medicineMeasureValue.getMedicineMeasure());
    }

    @Nullable
    public static String medicineMeasureValue(Context context,
                                              @Nullable Double amount,
                                              @Nullable MedicineMeasure medicineMeasure) {
        if (ObjectUtils.isPositive(amount)
                && medicineMeasure != null
                && !TextUtils.isEmpty(medicineMeasure.getName())) {
            String amountStr = DoubleUtils.multipleUnitFormat(amount);
            String medicineMeasureStr = medicineMeasure.getName();
            return context.getString(R.string.medicine_measure_format, amountStr, medicineMeasureStr);
        }
        return null;
    }

    @Nullable
    public static String frequency(Context context, @Nullable Integer number) {
        if (number == null) {
            return null;
        }
        if (number == 0) {
            return context.getString(R.string.frequency_once);
        }
        return numberOfTimes(context, number);
    }

    @Nullable
    public static String numberOfTimes(Context context, @Nullable Integer number) {
        if (number == null) {
            return null;
        }
        return context.getResources().getQuantityString(R.plurals.numberOfTimesInADay, number, number);
    }

    @Nullable
    public static String periodicity(Context context, @Nullable PeriodicityType type) {
        if (type == null) {
            return context.getString(R.string.no_value);
        }
        switch (type) {
            case DAILY:
                return context.getString(R.string.periodicity_daily);
            case WEEKLY:
                return context.getString(R.string.periodicity_weekly);
            case MONTHLY:
                return context.getString(R.string.periodicity_monthly);
        }
        return context.getString(R.string.no_value);
    }

    @Nullable
    public static String lengthValue(Context context,
                                     @Nullable LengthValue lengthValue) {
        return lengthValue == null
                ? context.getString(R.string.no_value)
                : lengthValue(context, lengthValue.getLength(), lengthValue.getTimeUnit());
    }

    @Nullable
    private static String lengthValue(Context context,
                                      @Nullable Integer length,
                                      @Nullable TimeUnit timeUnit) {
        if (length == null || timeUnit == null) {
            return context.getString(R.string.no_value);
        }
        switch (timeUnit) {
            case DAY:
                return context.getResources().getQuantityString(R.plurals.numberOfDays, length, length);
            case WEEK:
                return context.getResources().getQuantityString(R.plurals.numberOfWeeks, length, length);
            case MONTH:
                return context.getResources().getQuantityString(R.plurals.numberOfMonths, length, length);
        }
        return context.getString(R.string.no_value);
    }

    @Nullable
    public static String timeUnit(Context context,
                                  @Nullable Integer length,
                                  @Nullable TimeUnit timeUnit) {
        if (length == null || timeUnit == null) {
            return null;
        }
        switch (timeUnit) {
            case DAY:
                return context.getResources().getQuantityString(R.plurals.timeUnitNumberOfDays, length);
            case WEEK:
                return context.getResources().getQuantityString(R.plurals.timeUnitNumberOfWeeks, length);
            case MONTH:
                return context.getResources().getQuantityString(R.plurals.timeUnitNumberOfMonths, length);
        }
        return null;
    }
}
