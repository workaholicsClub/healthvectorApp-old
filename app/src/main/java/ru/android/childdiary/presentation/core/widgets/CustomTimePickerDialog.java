package ru.android.childdiary.presentation.core.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalTime;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class CustomTimePickerDialog extends TimePickerDialog {
    public static TimePickerDialog create(Context context, OnTimeSetListener callback,
                                          @Nullable LocalTime time, @Nullable Sex sex) {
        if (time == null) {
            time = LocalTime.now();
        }
        TimePickerDialog dialog = new CustomTimePickerDialog();
        dialog.initialize(callback, time.getHourOfDay(), time.getMinuteOfHour(), 0,
                DateFormat.is24HourFormat(context));
        dialog.vibrate(false);
        dialog.setVersion(TimePickerDialog.Version.VERSION_2);
        dialog.setAccentColor(ThemeUtils.getColorPrimary(context, sex));
        dialog.setOkText(R.string.ok);
        dialog.setCancelText(R.string.cancel);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            return null;
        }

        View viewOk = view.findViewById(R.id.ok);
        if (viewOk != null && viewOk instanceof Button) {
            ((Button) viewOk).setTypeface(FontUtils.getTypefaceRegular(getActivity()));
        }
        View viewCancel = view.findViewById(R.id.cancel);
        if (viewCancel != null && viewCancel instanceof Button) {
            ((Button) viewCancel).setTypeface(FontUtils.getTypefaceRegular(getActivity()));
        }

        return view;
    }
}
