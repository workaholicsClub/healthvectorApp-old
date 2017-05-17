package ru.android.childdiary.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.core.ContentObject;

public class ObjectUtils {
    public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static boolean contentEquals(@Nullable String s1, @Nullable String s2) {
        return TextUtils.isEmpty(s1) ? TextUtils.isEmpty(s2) : s1.equals(s2);
    }

    public static boolean isPositive(@Nullable Integer number) {
        return number != null && number > 0;
    }

    public static boolean isPositive(@Nullable Double number) {
        return number != null && number > 0;
    }

    public static boolean isTrue(@Nullable Boolean value) {
        return value != null && value;
    }

    public static boolean isFalse(@Nullable Boolean value) {
        return value == null || !value;
    }

    public static boolean equalsToMinutes(@Nullable DateTime dateTime1, @Nullable DateTime dateTime2) {
        if (dateTime1 == null) {
            return dateTime2 == null;
        }
        if (dateTime2 == null) {
            return false;
        }
        dateTime1 = dateTime1.withSecondOfMinute(0).withMillisOfSecond(0);
        dateTime2 = dateTime2.withSecondOfMinute(0).withMillisOfSecond(0);
        return dateTime1.isEqual(dateTime2);
    }

    public static boolean isEmpty(@Nullable ContentObject object) {
        return object == null || object.isContentEmpty();
    }

    public static <T> boolean contentEquals(@Nullable ContentObject<T> object1, @Nullable ContentObject<T> object2) {
        //noinspection unchecked
        return object1 == null
                ? object2 == null
                : object2 != null && object1.isContentEqual((T) object2);
    }
}
