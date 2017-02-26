package ru.android.childdiary.utils;

import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleUtils {
    private static final Logger logger = LoggerFactory.getLogger(DoubleUtils.class);

    @Nullable
    public static Double parse(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            logger.warn("failed to parse double", e);
            return null;
        }
    }

    public static String toString(Double d) {
        return d == null ? null : Double.toString(d);
    }
}
