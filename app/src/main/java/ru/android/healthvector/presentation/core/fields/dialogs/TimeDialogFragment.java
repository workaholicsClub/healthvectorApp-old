package ru.android.healthvector.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.TimeUnit;
import ru.android.healthvector.presentation.core.BaseMvpDialogFragment;
import ru.android.healthvector.utils.strings.StringUtils;
import ru.android.healthvector.utils.strings.TimeUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class TimeDialogFragment extends BaseMvpDialogFragment<TimeDialogArguments> {
    @BindView(R.id.numberPickerDays)
    NumberPicker numberPickerDays;

    @BindView(R.id.numberPickerHours)
    NumberPicker numberPickerHours;

    @BindView(R.id.durationSeparator)
    TextView durationSeparator;

    @BindView(R.id.numberPickerMinutes)
    NumberPicker numberPickerMinutes;

    @BindView(R.id.daysHeader)
    TextView daysHeader;

    @BindView(R.id.hoursHeader)
    TextView hoursHeader;

    @BindView(R.id.durationSeparatorHeader)
    TextView durationSeparatorHeader;

    @BindView(R.id.minutesHeader)
    TextView minutesHeader;

    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_time;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        TimeUtils.Time time = TimeUtils.splitMinutes(dialogArguments.getMinutes()).build();

        numberPickerDays.setMinValue(0);
        numberPickerDays.setMaxValue(Math.max(30, time.getDays()));
        numberPickerDays.setValue(time.getDays());
        numberPickerDays.setOnValueChangedListener((picker, oldVal, newVal) -> updateDaysHeader());
        updateDaysHeader();

        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(TimeUtils.HOURS_IN_DAY - 1);
        numberPickerHours.setValue(time.getHours());
        numberPickerHours.setOnValueChangedListener((picker, oldVal, newVal) -> updateHoursHeader());
        updateHoursHeader();

        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(TimeUtils.MINUTES_IN_HOUR - 1);
        numberPickerMinutes.setValue(time.getMinutes());
        numberPickerMinutes.setOnValueChangedListener((picker, oldVal, newVal) -> updateMinutesHeader());
        updateMinutesHeader();

        numberPickerDays.setVisibility(dialogArguments.isShowDays() ? View.VISIBLE : View.GONE);
        numberPickerHours.setVisibility(dialogArguments.isShowHours() ? View.VISIBLE : View.GONE);
        durationSeparator.setVisibility(dialogArguments.isShowHours() && dialogArguments.isShowMinutes() ? View.VISIBLE : View.GONE);
        numberPickerMinutes.setVisibility(dialogArguments.isShowMinutes() ? View.VISIBLE : View.GONE);

        daysHeader.setVisibility(dialogArguments.isShowDays() ? View.VISIBLE : View.GONE);
        hoursHeader.setVisibility(dialogArguments.isShowHours() ? View.VISIBLE : View.GONE);
        durationSeparatorHeader.setVisibility(dialogArguments.isShowHours() && dialogArguments.isShowMinutes() ? View.VISIBLE : View.GONE);
        minutesHeader.setVisibility(dialogArguments.isShowMinutes() ? View.VISIBLE : View.GONE);
    }

    private void updateDaysHeader() {
        daysHeader.setText(StringUtils.timeUnit(getContext(), numberPickerDays.getValue(), TimeUnit.DAY));
    }

    private void updateHoursHeader() {
        hoursHeader.setText(StringUtils.timeUnit(getContext(), numberPickerHours.getValue(), TimeUnit.HOUR));
    }

    private void updateMinutesHeader() {
        minutesHeader.setText(StringUtils.timeUnit(getContext(), numberPickerMinutes.getValue(), TimeUnit.MINUTE));
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
                            int resultMinutes = numberPickerDays.getValue() * TimeUtils.MINUTES_IN_DAY
                                    + numberPickerHours.getValue() * TimeUtils.MINUTES_IN_HOUR
                                    + numberPickerMinutes.getValue();
                            if (listener != null) {
                                listener.onSetTime(getTag(), resultMinutes);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus());

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface Listener {
        void onSetTime(String tag, int minutes);
    }
}
