package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;

public class WidgetsUtils {
    public static void setupTextView(TextView textView, boolean enabled) {
        @ColorRes int colorRes = enabled ? R.color.primary_text : R.color.placeholder_text;
        @ColorInt int color = ContextCompat.getColor(textView.getContext(), colorRes);
        textView.setTextColor(color);
    }

    public static void hideIfEmpty(TextView textView) {
        textView.setVisibility(TextUtils.isEmpty(textView.getText()) ? View.GONE : View.VISIBLE);
    }

    public static void setupCropActivityToolbar(Context context, UCrop.Options options, @Nullable Sex sex) {
        options.setToolbarColor(ThemeUtils.getColorPrimary(context, sex));
        options.setStatusBarColor(ThemeUtils.getColorPrimaryDark(context, sex));
        options.setToolbarTitle(context.getString(R.string.crop_image_title));
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

    @Nullable
    public static DateTime getDateTime(FieldDateView dateView, FieldTimeView timeView) {
        LocalDate date = dateView.getValue();
        LocalTime time = timeView.getValue(); // подставлять секунды, если время совпадает с текущим?
        return date == null || time == null
                ? null
                : new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour());
    }

    public static void setDateTime(@Nullable DateTime dateTime, FieldDateView dateView, FieldTimeView timeView) {
        dateView.setValue(dateTime == null ? null : dateTime.toLocalDate());
        timeView.setValue(dateTime == null ? null : dateTime.toLocalTime());
    }
}
