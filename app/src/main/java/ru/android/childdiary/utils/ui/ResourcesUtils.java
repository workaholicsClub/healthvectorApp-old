package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;

public class ResourcesUtils {
    public static Drawable getChildIcon(Context context, @NonNull Child child) {
        if (child.getImageFileName() == null) {
            return ContextCompat.getDrawable(context, R.drawable.child_profile_placeholder);
        }
        return Drawable.createFromPath(child.getImageFileName());
    }

    public static int[] getNavigationDrawerItemResources(Sex sex) {
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
    public static int getSelectedDateBackground(@Nullable Sex sex) {
        if (sex == null || sex == Sex.MALE) {
            return R.drawable.calendar_cell_background_selected_boy;
        } else {
            return R.drawable.calendar_cell_background_selected_girl;
        }
    }
}
