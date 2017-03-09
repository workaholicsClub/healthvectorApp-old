package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
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
                .themeDialogResourceId(R.style.AppThemeDialog_Boy)
                .colorPrimary(R.color.primary_boy)
                .colorPrimaryDark(R.color.primary_dark_boy)
                .colorAccent(R.color.accent_boy)
                .build());
        map.put(Sex.MALE, ThemeInfo.builder()
                .themeDialogResourceId(R.style.AppThemeDialog_Boy)
                .colorPrimary(R.color.primary_boy)
                .colorPrimaryDark(R.color.primary_dark_boy)
                .colorAccent(R.color.accent_boy)
                .build());
        map.put(Sex.FEMALE, ThemeInfo.builder()
                .themeDialogResourceId(R.style.AppThemeDialog_Girl)
                .colorPrimary(R.color.primary_girl)
                .colorPrimaryDark(R.color.primary_dark_girl)
                .colorAccent(R.color.accent_girl)
                .build());
    }

    @StyleRes
    public static int getThemeDialogRes(@Nullable Sex sex) {
        return map.get(sex).getThemeDialogResourceId();
    }

    @ColorRes
    public static int getColorPrimaryRes(@Nullable Sex sex) {
        return map.get(sex).getColorPrimary();
    }

    @ColorRes
    public static int getColorPrimaryDarkRes(@Nullable Sex sex) {
        return map.get(sex).getColorPrimaryDark();
    }

    @ColorRes
    public static int getColorAccentRes(@Nullable Sex sex) {
        return map.get(sex).getColorAccent();
    }

    @ColorInt
    public static int getColorPrimary(Context context, @Nullable Sex sex) {
        return ContextCompat.getColor(context, getColorPrimaryRes(sex));
    }

    @ColorInt
    public static int getColorPrimaryDark(Context context, @Nullable Sex sex) {
        return ContextCompat.getColor(context, getColorPrimaryDarkRes(sex));
    }

    @ColorInt
    public static int getColorAccent(Context context, @Nullable Sex sex) {
        return ContextCompat.getColor(context, getColorAccentRes(sex));
    }

    public static Drawable getColorPrimaryDrawable(Context context, @Nullable Sex sex) {
        return ContextCompat.getDrawable(context, getColorPrimaryRes(sex));
    }

    public static Drawable getColorPrimaryDarkDrawable(Context context, @Nullable Sex sex) {
        return ContextCompat.getDrawable(context, getColorPrimaryDarkRes(sex));
    }

    public static Drawable getColorAccentDrawable(Context context, @Nullable Sex sex) {
        return ContextCompat.getDrawable(context, getColorAccentRes(sex));
    }
}
