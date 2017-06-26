package ru.android.childdiary.presentation.calendar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.presentation.core.dialogs.BaseLengthValueDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MoveEventDialogFragment extends BaseLengthValueDialogFragment<MoveEventDialogArguments> {
    @Nullable
    private Listener listener;

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.move_event);

        if (dialogArguments.getEvent().getLinearGroup() == null) {
            builder.setPositiveButton(R.string.ok, (dialog, which) -> moveEventClick());
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> hideKeyboardAndClearFocus());
        } else {
            builder.setPositiveButton(R.string.move_one_event, (dialog, which) -> moveEventClick());
            builder.setNegativeButton(R.string.move_linear_group, (dialog, which) -> moveLinearGroupClick());
        }

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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
