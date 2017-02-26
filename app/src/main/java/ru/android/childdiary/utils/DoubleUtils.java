package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

import ru.android.childdiary.R;

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
    public static Double parseHeight(String text) {
        try {
            return HEIGHT_FORMAT.get().parse(text).doubleValue();
        } catch (Exception e) {
            logger.warn("failed to parse height", e);
            return null;
        }
    }

    @Nullable
    public static Double parseWeight(String text) {
        try {
            return WEIGHT_FORMAT.get().parse(text).doubleValue();
        } catch (Exception e) {
            logger.warn("failed to parse weight", e);
            return null;
        }
    }

    @Nullable
    public static String heightReview(Context context, Double d) {
        return d == null ? null : context.getString(R.string.height_review, HEIGHT_FORMAT.get().format(d));
    }

    @Nullable
    public static String weightReview(Context context, Double d) {
        return d == null ? null : context.getString(R.string.weight_review, WEIGHT_FORMAT.get().format(d));
    }

    @Nullable
    public static String heightEdit(Double d) {
        return d == null ? null : HEIGHT_FORMAT.get().format(d);
    }

    @Nullable
    public static String weightEdit(Double d) {
        return d == null ? null : WEIGHT_FORMAT.get().format(d);
    }
}
