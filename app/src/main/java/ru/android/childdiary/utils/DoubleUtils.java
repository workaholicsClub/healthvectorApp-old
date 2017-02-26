package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ru.android.childdiary.R;

public class DoubleUtils {
    private static final Logger logger = LoggerFactory.getLogger(DoubleUtils.class);

    private static final ThreadLocal<DecimalFormat> HEIGHT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setDecimalSeparator('.');
            return new DecimalFormat("0.#", dfs);
        }
    };

    private static final ThreadLocal<DecimalFormat> WEIGHT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setDecimalSeparator('.');
            return new DecimalFormat("0.###", dfs);
        }
    };

    @Nullable
    public static Double parse(String text) {
        String[] parts = text.split(" ");
        for (String part : parts) {
            try {
                double d = Double.parseDouble(part);
                return d;
            } catch (Exception e) {
                logger.warn("failed to parse double", e);
                return null;
            }
        }
        logger.warn("double not found");
        return null;
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
