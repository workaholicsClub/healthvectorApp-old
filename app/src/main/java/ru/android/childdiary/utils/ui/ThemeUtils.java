package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import ru.android.childdiary.domain.interactors.child.Child;

public class ThemeUtils {
    private static final Map<Sex, ThemeInfo> map = new HashMap<>();

    static {
        map.put(null, ThemeInfo.builder()
                .themeResourceId(R.style.AppTheme)
                .themeDialogResourceId(R.style.AppThemeDialog)
                .colorPrimary(R.color.primary_boy)
                .colorPrimaryDark(R.color.primary_dark_boy)
                .colorAccent(R.color.accent_boy)
                .build());
        map.put(Sex.MALE, ThemeInfo.builder()
                .themeResourceId(R.style.AppTheme_Boy)
                .themeDialogResourceId(R.style.AppThemeDialog_Boy)
                .colorPrimary(R.color.primary_boy)
                .colorPrimaryDark(R.color.primary_dark_boy)
                .colorAccent(R.color.accent_boy)
                .build());
        map.put(Sex.FEMALE, ThemeInfo.builder()
                .themeResourceId(R.style.AppTheme_Girl)
                .themeDialogResourceId(R.style.AppThemeDialog_Girl)
                .colorPrimary(R.color.primary_girl)
                .colorPrimaryDark(R.color.primary_dark_girl)
                .colorAccent(R.color.accent_girl)
                .build());
    }

    @StyleRes
    public static int getTheme(@Nullable Sex sex) {
        return map.get(sex).getThemeResourceId();
    }

    @StyleRes
    public static int getThemeDialog(@Nullable Sex sex) {
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

    public static Drawable getChildIcon(Context context, @Nullable Child child) {
        if (child == null) {
            return getChildDefaultIcon(context, null);
        }
        if (child.getImageFileName() == null) {
            return getChildDefaultIcon(context, child.getSex());
        }
        return Drawable.createFromPath(child.getImageFileName());
    }

    private static Drawable getChildDefaultIcon(Context context, @Nullable Sex sex) {
        // TODO: default icon for girl and boy
        return getColorPrimaryDarkDrawable(context, sex);
    }

    @DrawableRes
    public static int getButtonBackgroundRes(@Nullable Sex sex, boolean enabled) {
        if (sex == null) {
            return enabled ? R.drawable.button_background : R.drawable.button_background_disabled;
        }

        switch (sex) {
            case MALE:
                return enabled ? R.drawable.button_background_boy : R.drawable.button_background_boy_disabled;
            case FEMALE:
                return enabled ? R.drawable.button_background_girl : R.drawable.button_background_girl_disabled;
            default:
                return enabled ? R.drawable.button_background : R.drawable.button_background_disabled;
        }
    }
}
