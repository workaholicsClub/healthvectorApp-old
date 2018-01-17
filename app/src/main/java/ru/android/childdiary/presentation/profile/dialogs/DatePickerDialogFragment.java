package ru.android.childdiary.presentation.profile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import java.util.Calendar;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class DatePickerDialogFragment extends BaseMvpDialogFragment<DatePickerDialogArguments> {
    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @Nullable
    private Listener listener;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_date_picker;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LocalDate date = dialogArguments.getDate();
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date.toDate());
        }
        datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
        );
        if (dialogArguments.getMinDate() != null) {
            datePicker.setMaxDate(dialogArguments.getMinDate().toDate().getTime());
        }
        if (dialogArguments.getMaxDate() != null) {
            datePicker.setMaxDate(dialogArguments.getMaxDate().toDate().getTime());
        }
        WidgetsUtils.applyStyling(datePicker, ThemeUtils.getColorPrimary(getContext(), dialogArguments.getSex()));
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(dialogArguments.getTitle())
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus();
                            if (listener != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, datePicker.getYear());
                                calendar.set(Calendar.MONTH, datePicker.getMonth());
                                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                                LocalDate date = LocalDate.fromCalendarFields(calendar);
                                listener.onDatePick(getTag(), date);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus());

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (getParentFragment() instanceof Listener) {
            listener = (Listener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface Listener {
        void onDatePick(String tag, @NonNull LocalDate date);
    }
}
