package ru.android.childdiary.utils;

import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class DoubleUtils {
    private static final Logger logger = LoggerFactory.getLogger(DoubleUtils.class);

    private static final ThreadLocal<DecimalFormat> HEIGHT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.#");
        }
    };

    private static final ThreadLocal<DecimalFormat> WEIGHT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.###");
        }
    };

    @Nullable
    public static Double parse(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            logger.warn("failed to parse double", e);
            return null;
        }
    }

    @Nullable
    public static String height(Double d) {
        return d == null ? null : HEIGHT_FORMAT.get().format(d);
    }

    @Nullable
    public static String weight(Double d) {
        return d == null ? null : WEIGHT_FORMAT.get().format(d);
    }
}
