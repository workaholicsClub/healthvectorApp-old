package ru.android.healthvector.presentation.core.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.util.Calendar;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.utils.ui.FontUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomDatePickerDialog extends DatePickerDialog {
    public static DatePickerDialog create(Context context, OnDateSetListener callback,
                                          @Nullable LocalDate date, @Nullable Sex sex,
                                          @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date.toDate());
        }
        DatePickerDialog dialog = new CustomDatePickerDialog();
        dialog.initialize(callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.vibrate(false);
        dialog.setVersion(DatePickerDialog.Version.VERSION_2);
        dialog.setAccentColor(ThemeUtils.getColorPrimary(context, sex));
        dialog.setOkText(R.string.ok);
        dialog.setCancelText(R.string.cancel);

        if (minDate != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(minDate.toDate());
            dialog.setMinDate(calendar);
        }
        if (maxDate != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(maxDate.toDate());
            dialog.setMaxDate(calendar);
        }

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            return null;
        }

        View viewOk = view.findViewById(R.id.mdtp_ok);
        if (viewOk != null && viewOk instanceof Button) {
            ((Button) viewOk).setTypeface(FontUtils.getTypefaceRegular(getActivity()));
        }
        View viewCancel = view.findViewById(R.id.mdtp_cancel);
        if (viewCancel != null && viewCancel instanceof Button) {
            ((Button) viewCancel).setTypeface(FontUtils.getTypefaceRegular(getActivity()));
        }

        return view;
    }
}
