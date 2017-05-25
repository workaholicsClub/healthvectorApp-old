package ru.android.childdiary.presentation.core.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MoveEventDialogFragment extends BaseMvpDialogFragment<MoveEventDialogArguments> {
    private static final int[] MINUTES = new int[]{5, 10, 15, 30, 45, 60, 90, 120};

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    @Nullable
    private Listener listener;

    private List<TimeShift> timeShifts = new ArrayList<>();

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_move_event;
    }

    @Override
    protected void setupUi() {
        timeShifts.clear();
        for (int i = 0; i < MINUTES.length; ++i) {
            int minutes = MINUTES[i];
            TimeShift timeShift = TimeShift.builder()
                    .minutes(minutes)
                    .build();
            timeShifts.add(timeShift);
            timeShift = TimeShift.builder()
                    .minutes(-minutes)
                    .build();
            timeShifts.add(0, timeShift);
        }

        String[] names = Observable.fromIterable(timeShifts)
                .map(timeShift -> timeShift.getText(getContext()))
                .toList()
                .map(strings -> strings.toArray(new String[strings.size()]))
                .blockingGet();
        int index = names.length / 2;

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(names.length - 1);
        numberPicker.setDisplayedValues(names);

        numberPicker.setValue(index);
    }

    private TimeShift readTimeShift() {
        int index = numberPicker.getValue();
        return timeShifts.get(index);
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.move_event);

        if (dialogArguments.getEvent().getLinearGroup() == null) {
            builder.setPositiveButton(R.string.ok, (dialog, which) -> moveEventClick());
            builder.setNegativeButton(R.string.cancel, null);
        } else {
            builder.setPositiveButton(R.string.move_one_event, (dialog, which) -> moveEventClick());
            builder.setNegativeButton(R.string.move_linear_group, (dialog, which) -> moveLinearGroupClick());
        }

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void moveEventClick() {
        MasterEvent event = dialogArguments.getEvent();
        TimeShift timeShift = readTimeShift();
        if (listener != null) {
            listener.onMoveEventClick(getTag(), event, timeShift.getMinutes());
        }
    }

    private void moveLinearGroupClick() {
        MasterEvent event = dialogArguments.getEvent();
        TimeShift timeShift = readTimeShift();
        if (listener != null) {
            listener.onMoveLinearGroupClick(getTag(), event, timeShift.getMinutes());
        }
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
        void onMoveEventClick(String tag, @NonNull MasterEvent event, int minutes);

        void onMoveLinearGroupClick(String tag, @NonNull MasterEvent event, int minutes);
    }

    @Value
    @Builder
    private static class TimeShift {
        int minutes;

        @Nullable
        public String getText(Context context) {
            if (minutes == 0) {
                return null;
            }
            String text = TimeUtils.durationShort(context, Math.abs(minutes));
            if (minutes > 0) {
                return context.getString(R.string.move_time_format_positive, text);
            } else {
                return context.getString(R.string.move_time_format_negative, text);
            }
        }
    }
}
