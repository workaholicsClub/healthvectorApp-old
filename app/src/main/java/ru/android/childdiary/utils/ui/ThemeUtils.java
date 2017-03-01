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
    static int getColorPrimary(Context context, @Nullable Sex sex) {
        return getColor(context, getColorPrimaryRes(sex));
    }

    @ColorInt
    static int getColorPrimaryDark(Context context, @Nullable Sex sex) {
        return getColor(context, getColorPrimaryDarkRes(sex));
    }

    @ColorInt
    static int getColorAccent(Context context, @Nullable Sex sex) {
        return getColor(context, getColorAccentRes(sex));
    }

    @ColorInt
    public static int getColor(Context context, @ColorRes int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, colorResId);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(colorResId);
        }
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, drawableResId);
        } else {
            //noinspection deprecation
            return context.getResources().getDrawable(drawableResId);
        }
    }

    @ColorInt
    public static int getToolbarColor(Context context, @Nullable Sex sex) {
        return getColorPrimary(context, sex);
    }

    @ColorInt
    public static int getStatusBarColor(Context context, @Nullable Sex sex) {
        return getColorPrimaryDark(context, sex);
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
        return getDrawable(context, getColorPrimaryDarkRes(sex));
    }

    public static Drawable getHeaderDrawable(Context context, @Nullable Sex sex) {
        return getDrawable(context, getColorPrimaryRes(sex));
    }

    @DrawableRes
    public static int getHeaderDrawableRes(@Nullable Sex sex) {
        return getColorPrimaryRes(sex);
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
