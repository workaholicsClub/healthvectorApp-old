package ru.android.healthvector.presentation.calendar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.presentation.core.dialogs.BaseLengthValueDialogFragment;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class MoveEventDialogFragment extends BaseLengthValueDialogFragment<MoveEventDialogArguments> {
    @Nullable
    private Listener listener;

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(dialogArguments.getTitle());

        if (dialogArguments.getEvent().getLinearGroup() == null) {
            builder.setPositiveButton(R.string.ok,
                    (dialog, which) -> moveEventClick());
            builder.setNegativeButton(R.string.cancel,
                    (dialog, which) -> hideKeyboardAndClearFocus());
        } else {
            builder.setPositiveButton(R.string.move_one_event,
                    (dialog, which) -> moveEventClick());
            builder.setNegativeButton(R.string.move_linear_group,
                    (dialog, which) -> moveLinearGroupClick());
        }

        Dialog dialog = builder.create();
        dialog.setCancelable(dialogArguments.isCancelable());
        dialog.setCanceledOnTouchOutside(dialogArguments.isCancelable());
        return dialog;
    }

    private void moveEventClick() {
        hideKeyboardAndClearFocus();
        if (listener != null) {
            LengthValue lengthValue = readLengthValue();
            listener.onMoveEventClick(getTag(), dialogArguments.getEvent(), lengthValue.getMinutes());
        }
    }

    private void moveLinearGroupClick() {
        hideKeyboardAndClearFocus();
        if (listener != null) {
            LengthValue lengthValue = readLengthValue();
            listener.onMoveLinearGroupClick(getTag(), dialogArguments.getEvent(), lengthValue.getMinutes());
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
}
