package ru.android.childdiary.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleUtils {
    private static final Logger logger = LoggerFactory.getLogger(DoubleUtils.class);

    public static double parse(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            logger.warn("failed to parse double", e);
            return 0;
        }
    }

    public static String toString(Double d) {
        return d == null ? "" : Double.toString(d);
    }
}
