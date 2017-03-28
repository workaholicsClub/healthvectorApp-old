package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public class ResourcesUtils {
    public static Drawable getChildIcon(Context context, @NonNull Child child, boolean toolbar) {
        if (child.getImageFileName() == null) {
            return ContextCompat.getDrawable(context,
                    toolbar ? R.drawable.ic_placeholder_toolbar : R.drawable.ic_placeholder);
        }
        return Drawable.createFromPath(child.getImageFileName());
    }

    public static int[] getNavigationDrawerItemResources(@Nullable Sex sex) {
        if (sex == null || sex == Sex.MALE) {
            return new int[]{
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
    public static int getEventColor(Context context, @Nullable Sex sex, @NonNull MasterEvent event) {
        return ContextCompat.getColor(context, getEventColorRes(sex, event));
    }

    @ColorRes
    private static int getEventColorRes(@Nullable Sex sex, @NonNull MasterEvent event) {
        if (event.getEventType() == null) {
            return R.color.white;
        }

        switch (event.getEventType()) {
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
            default:
                return R.color.white;
        }
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
                return R.drawable.ic_feed_type_pumped_milk;
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
    public static int getRadioRes(@Nullable Sex sex, boolean on) {
        return on ? (sex == null || sex == Sex.MALE ? R.drawable.radio_on_boy : R.drawable.radio_on_girl)
                : R.drawable.radio_off;
    }

    @DrawableRes
    public static int getCheckBoxRes(@Nullable Sex sex, boolean on) {
        return on ? (sex == null || sex == Sex.MALE ? R.drawable.checkbox_on_boy : R.drawable.checkbox_on_girl)
                : R.drawable.checkbox_off;
    }

    @ColorInt
    public static int getTimerTextColor(Context context, @Nullable Sex sex, boolean on) {
        return ContextCompat.getColor(context, getTimerTextColorRes(sex, on));
    }

    @ColorRes
    private static int getTimerTextColorRes(@Nullable Sex sex, boolean on) {
        return sex == null || sex == Sex.MALE
                ? (on ? R.color.timer_on_boy : R.color.timer_off_boy)
                : (on ? R.color.timer_on_girl : R.color.timer_off_girl);
    }

    @DrawableRes
    public static int getTimerIcon(@Nullable Sex sex, boolean on) {
        return sex == null || sex == Sex.MALE
                ? (on ? R.drawable.timer_on_boy : R.drawable.timer_off_boy)
                : (on ? R.drawable.timer_on_girl : R.drawable.timer_off_girl);
    }

    @DrawableRes
    public static int getTimerBackgroundRes(@Nullable Sex sex, boolean on) {
        return sex == null || sex == Sex.MALE
                ? (on ? R.drawable.background_timer_on_boy : R.drawable.background_timer_off_boy)
                : (on ? R.drawable.background_timer_on_girl : R.drawable.background_timer_off_girl);
    }

    @DrawableRes
    public static int getNotificationSleepRes(@Nullable Sex sex) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return sex == null || sex == Sex.MALE
                    ? R.drawable.ic_notification_sleep_pre_lollipop_boy
                    : R.drawable.ic_notification_sleep_pre_lollipop_girl;
        } else {
            return R.drawable.ic_notification_sleep;
        }
    }
}
