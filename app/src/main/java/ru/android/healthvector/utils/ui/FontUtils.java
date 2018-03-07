package ru.android.healthvector.utils.ui;

import android.content.Context;
import android.graphics.Typeface;

import ru.android.healthvector.R;

public class FontUtils {
    private static Typeface typefaceRegular, typefaceBold, typefaceMedium;

    public static Typeface getTypefaceRegular(Context context) {
        if (typefaceRegular == null) {
            typefaceRegular = getTypeface(context, getFontPathRegular(context));
        }
        return typefaceRegular;
    }

    public static Typeface getTypefaceBold(Context context) {
        if (typefaceBold == null) {
            typefaceBold = getTypeface(context, getFontPathBold(context));
        }
        return typefaceBold;
    }

    public static Typeface getTypefaceMedium(Context context) {
        if (typefaceMedium == null) {
            typefaceMedium = getTypeface(context, getFontPathMedium(context));
        }
        return typefaceMedium;
    }

    public static String getFontPathRegular(Context context) {
        return context.getString(R.string.font_path_regular);
    }

    public static String getFontPathBold(Context context) {
        return context.getString(R.string.font_path_bold);
    }

    public static String getFontPathMedium(Context context) {
        return context.getString(R.string.font_path_medium);
    }

    private static Typeface getTypeface(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }
}
