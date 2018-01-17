package ru.android.childdiary.presentation.profile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class TimePickerDialogFragment extends BaseMvpDialogFragment<TimePickerDialogArguments> {
    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @Nullable
    private Listener listener;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_time_picker;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LocalTime time = dialogArguments.getTime();
        if (time == null) {
            time = LocalTime.now();
        }
        int h = time.getHourOfDay();
        int m = time.getMinuteOfHour();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(h);
            timePicker.setMinute(m);
        } else {
            //noinspection deprecation
            timePicker.setCurrentHour(h);
            //noinspection deprecation
            timePicker.setCurrentMinute(m);
        }
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        WidgetsUtils.applyStyling(timePicker, ThemeUtils.getColorPrimary(getContext(), dialogArguments.getSex()));
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
                                int h, m;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    h = timePicker.getHour();
                                    m = timePicker.getMinute();
                                } else {
                                    //noinspection deprecation
                                    h = timePicker.getCurrentHour();
                                    //noinspection deprecation
                                    m = timePicker.getCurrentMinute();
                                }
                                LocalTime time = new LocalTime(h, m);
                                listener.onTimePick(getTag(), time);
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
        void onTimePick(String tag, @NonNull LocalTime time);
    }
}
