package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yalantis.ucrop.UCrop;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;

public class WidgetsUtils {
    private static final Logger logger = LoggerFactory.getLogger(WidgetsUtils.class);

    public static void setupTextView(TextView textView, boolean enabled) {
        @ColorRes int colorRes = enabled ? R.color.primary_text : R.color.placeholder_text;
        @ColorInt int color = ContextCompat.getColor(textView.getContext(), colorRes);
        textView.setTextColor(color);
    }

    public static void strikeTextView(TextView textView, boolean strike) {
        if (strike) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
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

    public static void setupSearchView(SearchView searchView, int maxLength) {
        @IdRes int resId;
        ImageView imageView;
        Context context = searchView.getContext();

        resId = android.support.v7.appcompat.R.id.search_button;
        imageView = ButterKnife.findById(searchView, resId);
        if (imageView != null) {
            imageView.setImageResource(R.drawable.toolbar_action_search);
        }

        resId = android.support.v7.appcompat.R.id.search_close_btn;
        imageView = ButterKnife.findById(searchView, resId);
        if (imageView != null) {
            imageView.setImageResource(R.drawable.toolbar_action_close);
        }

        resId = android.support.v7.appcompat.R.id.search_src_text;
        TextView textView = ButterKnife.findById(searchView, resId);
        //noinspection deprecation
        textView.setTextAppearance(context, R.style.SearchTextAppearance);
        textView.setTypeface(FontUtils.getTypefaceRegular(context));
        textView.setHintTextColor(ContextCompat.getColor(context, R.color.white_transparent));
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public static void applyStyling(TimePicker timePicker, @ColorInt int color) {
        setNumberPickerDividerColor(timePicker, "hour", color);
        setNumberPickerDividerColor(timePicker, "minute", color);
        setNumberPickerDividerColor(timePicker, "amPm", color);
    }

    public static void applyStyling(DatePicker datePicker, @ColorInt int color) {
        setNumberPickerDividerColor(datePicker, "month", color);
        setNumberPickerDividerColor(datePicker, "day", color);
        setNumberPickerDividerColor(datePicker, "year", color);
    }

    private static void setNumberPickerDividerColor(View view, String id, @ColorInt int color) {
        int monthNumberPickerId = Resources.getSystem().getIdentifier(id, "id", "android");
        NumberPicker numberPicker = ButterKnife.findById(view, monthNumberPickerId);
        if (numberPicker == null) {
            logger.warn("failed to setup number picker divider color: number picker with id '" + id + "' not found");
            return;
        }
        setNumberPickerDividerColor(numberPicker, color);
    }

    private static void setNumberPickerDividerColor(@NonNull NumberPicker numberPicker, @ColorInt int color) {
        try {
            for (int i = 0; i < numberPicker.getChildCount(); ++i) {
                Field dividerField = numberPicker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(color);
                dividerField.set(numberPicker, colorDrawable);
            }
            numberPicker.invalidate();
        } catch (Exception e) {
            logger.warn("failed to setup number picker divider color", e);
        }
    }
}
