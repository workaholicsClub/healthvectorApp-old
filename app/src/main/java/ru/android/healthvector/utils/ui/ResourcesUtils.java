package ru.android.healthvector.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.data.types.FeedType;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.child.data.Child;

public class ResourcesUtils {
    @Nullable
    public static Drawable getChildIconForToolbar(Context context, @NonNull Child child) {
        return getChildIcon(context, child,
                ContextCompat.getDrawable(context, R.drawable.ic_placeholder_toolbar));
    }

    @Nullable
    public static Drawable getChildIconForAccountHeader(Context context, @NonNull Child child) {
        return getChildIcon(context, child,
                ContextCompat.getDrawable(context, R.drawable.ic_placeholder_account_header));
    }

    @Nullable
    public static Drawable getChildIconForSettings(Context context, @NonNull Child child) {
        return getChildIcon(context, child,
                ContextCompat.getDrawable(context, R.drawable.ic_placeholder_account_header));
    }

    @Nullable
    public static Drawable getChildIconForProfile(Context context, @NonNull Child child) {
        return getChildIcon(context, child,
                ContextCompat.getDrawable(context, R.color.white));
    }

    @Nullable
    private static Drawable getChildIcon(Context context,
                                         @NonNull Child child,
                                         @Nullable Drawable placeholder) {
        return getPhotoDrawable(context, child.getImageFileName(), placeholder);
    }

    @Nullable
    public static Drawable getPhotoDrawable(Context context,
                                            @Nullable String imageFileName) {
        return getPhotoDrawable(context, imageFileName, null);
    }

    @Nullable
    public static Drawable getPhotoDrawable(Context context,
                                            @Nullable String imageFileName,
                                            @Nullable Drawable placeholder) {
        if (TextUtils.isEmpty(imageFileName)) {
            return placeholder;
        }
        File file = new File(context.getFilesDir(), imageFileName);
        return Drawable.createFromPath(file.getAbsolutePath());
    }

    public static int[] getNavigationDrawerItemResources(@Nullable Sex sex) {
        if (sex == null || sex == Sex.MALE) {
            return new int[]{
                    R.drawable.navigation_drawer_item_calendar_boy,
                    R.drawable.navigation_drawer_item_calendar_boy,
                    R.drawable.navigation_drawer_item_development_diary_boy,
                    R.drawable.navigation_drawer_item_exercises_boy,
                    R.drawable.navigation_drawer_item_medical_data_boy,
                    R.drawable.navigation_drawer_item_settings_boy,
                    R.drawable.navigation_drawer_item_help_boy
            };
        } else {
            return new int[]{
                    R.drawable.navigation_drawer_item_calendar_girl,
                    R.drawable.navigation_drawer_item_calendar_girl,
                    R.drawable.navigation_drawer_item_development_diary_girl,
                    R.drawable.navigation_drawer_item_exercises_girl,
                    R.drawable.navigation_drawer_item_medical_data_girl,
                    R.drawable.navigation_drawer_item_settings_girl,
                    R.drawable.navigation_drawer_item_help_girl
            };
        }
    }

    @DrawableRes
    public static int getButtonBackgroundRes(@Nullable Sex sex, boolean enabled) {
        if (sex == null || sex == Sex.MALE) {
            return enabled ? R.drawable.button_background_boy : R.drawable.button_background_boy_disabled;
        } else {
            return enabled ? R.drawable.button_background_girl : R.drawable.button_background_girl_disabled;
        }
    }

    @DrawableRes
    public static int getSelectedDateBackgroundRes(@Nullable Sex sex) {
        if (sex == null || sex == Sex.MALE) {
            return R.drawable.calendar_cell_background_selected_boy;
        } else {
            return R.drawable.calendar_cell_background_selected_girl;
        }
    }

    @ColorInt
    public static int getEventColor(Context context, @Nullable Sex sex, @NonNull EventType eventType) {
        return ContextCompat.getColor(context, getEventColorRes(sex, eventType));
    }

    @ColorRes
    private static int getEventColorRes(@Nullable Sex sex, @NonNull EventType eventType) {
        switch (eventType) {
            case DIAPER:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_diaper_row_background_boy
                        : R.color.event_diaper_row_background_girl;
            case SLEEP:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_sleep_row_background_boy
                        : R.color.event_sleep_row_background_girl;
            case FEED:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_feed_row_background_boy
                        : R.color.event_feed_row_background_girl;
            case PUMP:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_pump_row_background_boy
                        : R.color.event_pump_row_background_girl;
            case OTHER:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_other_row_background_boy
                        : R.color.event_other_row_background_girl;
            case DOCTOR_VISIT:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_doctor_visit_row_background_boy
                        : R.color.event_doctor_visit_row_background_girl;
            case MEDICINE_TAKING:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_medicine_taking_row_background_boy
                        : R.color.event_medicine_taking_row_background_girl;
            case EXERCISE:
                return sex == null || sex == Sex.MALE
                        ? R.color.event_exercise_row_background_boy
                        : R.color.event_exercise_row_background_girl;
            default:
                return R.color.gray_background;
        }
    }

    @DrawableRes
    public static int getCircleButtonRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.button_circle_background_boy
                : R.drawable.button_circle_background_girl;
    }

    @DrawableRes
    public static int getDiaperEventLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_diaper_boy
                : R.drawable.toolbar_logo_diaper_girl;
    }

    @DrawableRes
    public static int getFeedTypeIcon(@Nullable FeedType feedType) {
        if (feedType == null) {
            return R.drawable.ic_feed_type;
        }

        switch (feedType) {
            case BREAST_MILK:
                return R.drawable.ic_feed_type_breast_milk;
            case PUMPED_MILK:
                return R.drawable.ic_feed_type;
            case MILK_FORMULA:
                return R.drawable.ic_feed_type_milk_formula;
            case FOOD:
                return R.drawable.ic_feed_type_food;
            default:
                return R.drawable.ic_feed_type;
        }
    }

    @DrawableRes
    public static int getFeedEventLogoRes(@Nullable Sex sex, @Nullable FeedType feedType) {
        if (feedType == null) {
            return sex == null || sex == Sex.MALE
                    ? R.drawable.toolbar_logo_feed_boy
                    : R.drawable.toolbar_logo_feed_girl;
        }

        switch (feedType) {
            case BREAST_MILK:
                return sex == null || sex == Sex.MALE
                        ? R.drawable.toolbar_logo_feed_breast_milk_boy
                        : R.drawable.toolbar_logo_feed_breast_milk_girl;
            case PUMPED_MILK:
                return sex == null || sex == Sex.MALE
                        ? R.drawable.toolbar_logo_feed_pumped_milk_boy
                        : R.drawable.toolbar_logo_feed_pumped_milk_girl;
            case MILK_FORMULA:
                return sex == null || sex == Sex.MALE
                        ? R.drawable.toolbar_logo_feed_milk_formula_boy
                        : R.drawable.toolbar_logo_feed_milk_formula_girl;
            case FOOD:
                return sex == null || sex == Sex.MALE
                        ? R.drawable.toolbar_logo_feed_food_boy
                        : R.drawable.toolbar_logo_feed_food_girl;
            default:
                return sex == null || sex == Sex.MALE
                        ? R.drawable.toolbar_logo_feed_boy
                        : R.drawable.toolbar_logo_feed_girl;
        }
    }

    @DrawableRes
    public static int getOtherEventLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_other_boy
                : R.drawable.toolbar_logo_other_girl;
    }

    @DrawableRes
    public static int getPumpEventLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_pump_boy
                : R.drawable.toolbar_logo_pump_girl;
    }

    @DrawableRes
    public static int getSleepEventLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_sleep_boy
                : R.drawable.toolbar_logo_sleep_girl;
    }

    @DrawableRes
    public static int getDoctorVisitLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_doctor_visit_boy
                : R.drawable.toolbar_logo_doctor_visit_girl;
    }

    @DrawableRes
    public static int getMedicineTakingLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_medicine_taking_boy
                : R.drawable.toolbar_logo_medicine_taking_girl;
    }

    @DrawableRes
    public static int getExerciseLogoRes(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.toolbar_logo_exercise_boy
                : R.drawable.toolbar_logo_exercise_girl;
    }

    @DrawableRes
    public static int getRadioRes(@Nullable Sex sex, boolean on) {
        return on ? (sex == null || sex == Sex.MALE ? R.drawable.radio_on_boy : R.drawable.radio_on_girl)
                : R.drawable.radio_off;
    }

    @DrawableRes
    public static int getCheckBoxRes(@Nullable Sex sex, boolean on, boolean enabled) {
        if (enabled) {
            return on ? (sex == null || sex == Sex.MALE ? R.drawable.checkbox_on_boy : R.drawable.checkbox_on_girl)
                    : R.drawable.checkbox_off;
        }
        return on ? R.drawable.checkbox_on_disabled : R.drawable.checkbox_off_disabled;
    }

    @ColorInt
    public static int getTimerTextColor(Context context, @Nullable Sex sex, boolean on) {
        return ContextCompat.getColor(context, getTimerTextColorRes(sex, on));
    }

    @ColorRes
    private static int getTimerTextColorRes(@Nullable Sex sex, boolean on) {
        return sex == null || sex == Sex.MALE
                ? (on ? R.color.timer_on : R.color.timer_off)
                : (on ? R.color.timer_on : R.color.timer_off);
    }

    @DrawableRes
    public static int getTimerIcon(@Nullable Sex sex, boolean on) {
        return sex == null || sex == Sex.MALE
                ? (on ? R.drawable.timer_on : R.drawable.timer_off)
                : (on ? R.drawable.timer_on : R.drawable.timer_off);
    }

    @DrawableRes
    public static int getTimerBackgroundRes(@Nullable Sex sex, boolean on) {
        return sex == null || sex == Sex.MALE
                ? (on ? R.drawable.background_timer_on : R.drawable.background_timer_off)
                : (on ? R.drawable.background_timer_on : R.drawable.background_timer_off);
    }

    @DrawableRes
    public static int getTimeItemBackgroundRes(@Nullable Sex sex, boolean enabled) {
        return sex == null || sex == Sex.MALE
                ? (enabled ? R.drawable.background_times_on_boy : R.drawable.background_times_off)
                : (enabled ? R.drawable.background_times_on_girl : R.drawable.background_times_off);
    }

    @DrawableRes
    public static int getNotificationEventRes(@Nullable Child child) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Sex sex = child == null ? null : child.getSex();
            return sex == null || sex == Sex.MALE
                    ? R.drawable.ic_notification_event_pre_lollipop_boy
                    : R.drawable.ic_notification_event_pre_lollipop_girl;
        } else {
            return R.drawable.ic_notification_event;
        }
    }

    @DrawableRes
    public static int getNotificationLinearGroupRes(@Nullable Child child) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Sex sex = child == null ? null : child.getSex();
            return sex == null || sex == Sex.MALE
                    ? R.drawable.ic_notification_linear_group_pre_lollipop_boy
                    : R.drawable.ic_notification_linear_group_pre_lollipop_girl;
        } else {
            return R.drawable.ic_notification_linear_group;
        }
    }

    @DrawableRes
    public static int getNotificationSleepRes(@Nullable Child child) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Sex sex = child == null ? null : child.getSex();
            return sex == null || sex == Sex.MALE
                    ? R.drawable.ic_notification_sleep_pre_lollipop_boy
                    : R.drawable.ic_notification_sleep_pre_lollipop_girl;
        } else {
            return R.drawable.ic_notification_sleep;
        }
    }

    @DrawableRes
    public static int getExerciseIcon(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.ic_exercise_boy : R.drawable.ic_exercise_girl;
    }

    @DrawableRes
    public static int getExerciseExportedIcon(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.drawable.ic_exercise_exported_boy : R.drawable.ic_exercise_exported_girl;
    }

    @StyleRes
    public static int getDialogTitleTextAppearance(@Nullable Sex sex) {
        return sex == null || sex == Sex.MALE
                ? R.style.DialogTitleTextAppearanceBoy
                : R.style.DialogTitleTextAppearanceGirl;
    }

    public static GradientDrawable getShape(@ColorInt int color, float corner) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        shape.setColor(color);
        return shape;
    }
}
