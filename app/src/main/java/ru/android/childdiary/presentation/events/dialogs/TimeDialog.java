package ru.android.childdiary.presentation.events.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class TimeDialog extends BaseDialogFragment {
    private static final String KEY_PARAMETERS = "parameters";

    @BindView(R.id.numberPickerDays)
    NumberPicker numberPickerDays;

    @BindView(R.id.numberPickerHours)
    NumberPicker numberPickerHours;

    @BindView(R.id.numberPickerMinutes)
    NumberPicker numberPickerMinutes;

    @BindView(R.id.daysView)
    View daysView;

    @BindView(R.id.hoursView)
    View hoursView;

    @BindView(R.id.minutesView)
    View minutesView;

    @BindView(R.id.delimiter)
    View delimiter;

    @BindView(R.id.dummy)
    View dummy;

    private Listener listener;

    public void showAllowingStateLoss(FragmentManager manager, String tag, @NonNull Child child, Parameters parameters) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ExtraConstants.EXTRA_CHILD, child);
        arguments.putSerializable(KEY_PARAMETERS, parameters);
        setArguments(arguments);
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        init(savedInstanceState);

        Parameters parameters = (Parameters) getArguments().getSerializable(KEY_PARAMETERS);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_time, null);

        ButterKnife.bind(this, view);

        setupUi(parameters);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(sex))
                .setView(view)
                .setTitle(parameters.getTitle())
                .setPositiveButton(R.string.OK, (dialog, which) -> {
                    hideKeyboardAndClearFocus(view.findFocus());
                    int resultMinutes = numberPickerDays.getValue() * TimeUtils.MINUTES_IN_DAY
                            + numberPickerHours.getValue() * TimeUtils.MINUTES_IN_HOUR
                            + numberPickerMinutes.getValue();
                    if (listener != null) {
                        listener.onSetTime(getTag(), resultMinutes);
                    }
                })
                .setNegativeButton(R.string.Cancel, null);
        return builder.create();
    }

    private void setupUi(Parameters parameters) {
        TimeUtils.Time time = TimeUtils.splitMinutes(parameters.getMinutes());

        numberPickerDays.setMinValue(0);
        numberPickerDays.setMaxValue(Math.max(7, time.getDays()));
        numberPickerDays.setValue(time.getDays());

        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(TimeUtils.HOURS_IN_DAY - 1);
        numberPickerHours.setValue(time.getHours());

        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(TimeUtils.MINUTES_IN_HOUR - 1);
        numberPickerMinutes.setValue(time.getMinutes());

        daysView.setVisibility(parameters.isShowDays() ? View.VISIBLE : View.GONE);
        hoursView.setVisibility(parameters.isShowHours() ? View.VISIBLE : View.GONE);
        delimiter.setVisibility(parameters.isShowHours() && parameters.isShowMinutes() ? View.VISIBLE : View.GONE);
        minutesView.setVisibility(parameters.isShowMinutes() ? View.VISIBLE : View.GONE);
    }

    public void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(getContext(), view);
        view.clearFocus();
        dummy.requestFocus();
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

    @Value
    @Builder
    public static class Parameters implements Serializable {
        int minutes;
        String title;
        boolean showDays;
        boolean showHours;
        boolean showMinutes;
    }
}
