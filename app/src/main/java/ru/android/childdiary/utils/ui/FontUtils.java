package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.graphics.Typeface;

import ru.android.childdiary.R;

public class FontUtils {
    private static Typeface typefaceRegular, typefaceBold;

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

    public static String getFontPathRegular(Context context) {
        return context.getString(R.string.font_path_regular);
    }

    public static String getFontPathBold(Context context) {
        return context.getString(R.string.font_path_bold);
    }

    private static Typeface getTypeface(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }
}
