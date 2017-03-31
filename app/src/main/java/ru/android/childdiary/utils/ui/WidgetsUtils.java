package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;

public class WidgetsUtils {
    public static void setupTextView(TextView textView, boolean enabled) {
        @ColorRes int colorRes = enabled ? R.color.primary_text : R.color.placeholder_text;
        @ColorInt int color = ContextCompat.getColor(textView.getContext(), colorRes);
        textView.setTextColor(color);
    }

    public static void setupCropActivityToolbar(Context context, UCrop.Options options, @Nullable Sex sex) {
        options.setToolbarColor(ThemeUtils.getColorPrimary(context, sex));
        options.setStatusBarColor(ThemeUtils.getColorPrimaryDark(context, sex));
        options.setToolbarTitle(context.getString(R.string.crop_image_title));
    }

    public static void setupTimer(Context context, Button buttonTimer, @Nullable Sex sex, boolean on) {
        buttonTimer.setTextColor(ResourcesUtils.getTimerTextColor(context, sex, on));
        buttonTimer.setBackgroundResource(ResourcesUtils.getTimerBackgroundRes(sex, on));
        buttonTimer.setCompoundDrawablesWithIntrinsicBounds(ResourcesUtils.getTimerIcon(sex, on), 0, 0, 0);
    }

    public static void setupTabLayoutFont(TabLayout tabLayout) {
        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabs.getChildCount(); ++i) {
            ViewGroup tab = (ViewGroup) tabs.getChildAt(i);
            for (int j = 0; j < tab.getChildCount(); ++j) {
                View tabViewChild = tab.getChildAt(j);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(FontUtils.getTypefaceRegular(tabLayout.getContext()));
                }
            }
        }
    }
}
