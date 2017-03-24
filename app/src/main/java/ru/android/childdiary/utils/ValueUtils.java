package ru.android.childdiary.utils;

public class ValueUtils {
    public static boolean hasValue(Integer number) {
        return number != null && number != 0;
    }

    public static boolean hasValue(Double number) {
        return number != null && number != 0;
    }
}
