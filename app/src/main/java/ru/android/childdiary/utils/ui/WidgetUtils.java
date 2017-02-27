package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yalantis.ucrop.UCrop;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;

public class WidgetUtils {
    public static void setupTextView(TextView textView, boolean enabled) {
        @ColorRes int colorRes = enabled ? R.color.primary_text : R.color.secondary_text;
        @ColorInt int color = ThemeUtils.getColor(textView.getContext(), colorRes);
        textView.setTextColor(color);
    }

    public static void setupCropActivityToolbar(Context context, UCrop.Options options, @Nullable Sex sex) {
        options.setToolbarColor(ThemeUtils.getToolbarColor(context, sex));
        options.setStatusBarColor(ThemeUtils.getStatusBarColor(context, sex));
        options.setToolbarTitle(context.getString(R.string.crop_image_title));
    }

    public static void setupDatePicker(Context context, DatePickerDialog dpd, @Nullable Sex sex) {
        dpd.setAccentColor(ThemeUtils.getColorAccent(context, sex));
    }

    public static void setupTimePicker(Context context, TimePickerDialog tpd, @Nullable Sex sex) {
        tpd.setAccentColor(ThemeUtils.getColorAccent(context, sex));
    }
}
