package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public static DateTime getDateTime(@NonNull FieldDateView dateView, @Nullable FieldTimeView timeView) {
        LocalDate date = dateView.getValue();
        LocalTime time = timeView == null ? LocalTime.MIDNIGHT : timeView.getValue();
        return date == null || time == null
                ? null
                : new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour());
    }

    public static void setDateTime(@Nullable DateTime dateTime, @NonNull FieldDateView dateView, @Nullable FieldTimeView timeView) {
        dateView.setValue(dateTime == null ? null : dateTime.toLocalDate());
        if (timeView != null) {
            timeView.setValue(dateTime == null ? null : dateTime.toLocalTime());
        }
    }

    public static void setupSearchView(SearchView searchView) {
        @IdRes int resId;
        ImageView imageView;
        Context context = searchView.getContext();

        resId = android.support.v7.appcompat.R.id.search_button;
        imageView = (ImageView) searchView.findViewById(resId);
        if (imageView != null) {
            imageView.setImageResource(R.drawable.toolbar_action_search);
        }

        resId = android.support.v7.appcompat.R.id.search_close_btn;
        imageView = (ImageView) searchView.findViewById(resId);
        if (imageView != null) {
            imageView.setImageResource(R.drawable.toolbar_action_close);
        }

        resId = android.support.v7.appcompat.R.id.search_src_text;
        TextView textView = (TextView) searchView.findViewById(resId);
        //noinspection deprecation
        textView.setTextAppearance(context, R.style.SearchTextAppearance);
        textView.setTypeface(FontUtils.getTypefaceRegular(context));
        textView.setHintTextColor(ContextCompat.getColor(context, R.color.white_transparent));
    }
}
