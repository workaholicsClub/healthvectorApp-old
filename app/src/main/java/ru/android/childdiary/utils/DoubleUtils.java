package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ru.android.childdiary.R;

public class DoubleUtils {
    private static final ThreadLocal<DecimalFormat> SUBMULTIPLE_UNIT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("0.#", dfs);
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df;
        }
    };

    private static final ThreadLocal<DecimalFormat> MULTIPLE_UNIT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("0.###", dfs);
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df;
        }
    };

    @Nullable
    public static Double parse(String text) {
        String[] parts = text.split(" ");
        for (String part : parts) {
            try {
                double d = Double.parseDouble(part);
                return d;
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    @Nullable
    public static String heightReview(Context context, Double d) {
        return d == null ? null : context.getString(R.string.height_review, SUBMULTIPLE_UNIT_FORMAT.get().format(d));
    }

    @Nullable
    public static String weightReview(Context context, Double d) {
        return d == null ? null : context.getString(R.string.weight_review, MULTIPLE_UNIT_FORMAT.get().format(d));
    }

    @Nullable
    public static String amountReview(Double d) {
        return d == null ? null : SUBMULTIPLE_UNIT_FORMAT.get().format(d);
    }

    @Nullable
    public static String amountMlReview(Context context, Double d) {
        return d == null ? null : context.getString(R.string.amount_review, SUBMULTIPLE_UNIT_FORMAT.get().format(d));
    }

    @Nullable
    public static String heightEdit(Double d) {
        return d == null ? null : SUBMULTIPLE_UNIT_FORMAT.get().format(d);
    }

    @Nullable
    public static String weightEdit(Double d) {
        return d == null ? null : MULTIPLE_UNIT_FORMAT.get().format(d);
    }

    @Nullable
    public static String amountEdit(Double d) {
        return d == null ? null : SUBMULTIPLE_UNIT_FORMAT.get().format(d);
    }

    @Nullable
    public static String amountMlEdit(Double d) {
        return d == null ? null : SUBMULTIPLE_UNIT_FORMAT.get().format(d);
    }

    @Nullable
    public static String multipleUnitFormat(Double d) {
        return d == null ? null : MULTIPLE_UNIT_FORMAT.get().format(d);
    }

    @Nullable
    public static String submultipleUnitFormat(Double d) {
        return d == null ? null : SUBMULTIPLE_UNIT_FORMAT.get().format(d);
    }
}
