package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;

public class ThemeUtils {
    private static final Map<Sex, ThemeInfo> map = new HashMap<>();

    static {
        map.put(null, ThemeInfo.builder()
                .themeResourceId(R.style.AppTheme)
                .colorPrimary(R.color.primary_boy)
                .colorPrimaryDark(R.color.primary_dark_boy)
                .colorAccent(R.color.accent_boy)
                .build());
        map.put(Sex.MALE, ThemeInfo.builder()
                .themeResourceId(R.style.AppTheme_Boy)
                .colorPrimary(R.color.primary_boy)
                .colorPrimaryDark(R.color.primary_dark_boy)
                .colorAccent(R.color.accent_boy)
                .build());
        map.put(Sex.FEMALE, ThemeInfo.builder()
                .themeResourceId(R.style.AppTheme_Girl)
                .colorPrimary(R.color.primary_girl)
                .colorPrimaryDark(R.color.primary_dark_girl)
                .colorAccent(R.color.accent_girl)
                .build());
    }

    @StyleRes
    public static int getTheme(@Nullable Sex sex) {
        return map.get(sex).getThemeResourceId();
    }

    @ColorRes
    private static int getColorPrimaryRes(@Nullable Sex sex) {
        return map.get(sex).getColorPrimary();
    }

    @ColorRes
    private static int getColorPrimaryDarkRes(@Nullable Sex sex) {
        return map.get(sex).getColorPrimaryDark();
    }

    @ColorRes
    private static int getColorAccentRes(@Nullable Sex sex) {
        return map.get(sex).getColorAccent();
    }

    @ColorInt
    public static int getColorPrimary(Context context, @Nullable Sex sex) {
        return getColor(context, getColorPrimaryRes(sex));
    }

    @ColorInt
    public static int getColorPrimaryDark(Context context, @Nullable Sex sex) {
        return getColor(context, getColorPrimaryDarkRes(sex));
    }

    @ColorInt
    public static int getColorAccent(Context context, @Nullable Sex sex) {
        return getColor(context, getColorAccentRes(sex));
    }

    @ColorInt
    static int getColor(Context context, @ColorRes int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, colorResId);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(colorResId);
        }
    }

    public static Drawable getChildDefaultIcon(Context context, @Nullable Sex sex) {
        // TODO: default icon for girl and boy
        return getDrawable(context, getColorPrimaryDarkRes(sex));
    }

    public static Drawable getHeaderDrawable(Context context, @Nullable Sex sex) {
        return getDrawable(context, getColorPrimaryRes(sex));
    }

    private static Drawable getDrawable(Context context, @DrawableRes int drawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, drawableResId);
        } else {
            //noinspection deprecation
            return context.getResources().getDrawable(drawableResId);
        }
    }
}
