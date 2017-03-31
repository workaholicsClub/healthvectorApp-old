package ru.android.childdiary.presentation.core.widgets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import ru.android.childdiary.utils.ui.WidgetsUtils;

public class CustomDatePickerDialog extends DatePickerDialog {
    public static DatePickerDialog newInstance(OnDateSetListener callBack, int year,
                                               int monthOfYear,
                                               int dayOfMonth) {
        DatePickerDialog dialog = new CustomDatePickerDialog();
        dialog.initialize(callBack, year, monthOfYear, dayOfMonth);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        WidgetsUtils.setupDatePickerAfterShow(getActivity(), view);
        return view;
    }
}
