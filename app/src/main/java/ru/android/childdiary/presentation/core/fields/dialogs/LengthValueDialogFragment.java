package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.presentation.core.dialogs.BaseLengthValueDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class LengthValueDialogFragment extends BaseLengthValueDialogFragment<LengthValueDialogArguments> {
    @Nullable
    private Listener listener;

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(dialogArguments.getTitle())
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus();
                            if (listener != null) {
                                listener.onSetLengthValue(getTag(), readLengthValue());
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus());

        Dialog dialog = builder.create();
        dialog.setCancelable(dialogArguments.isCancelable());
        dialog.setCanceledOnTouchOutside(dialogArguments.isCancelable());
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
        void onSetLengthValue(String tag, @NonNull LengthValue lengthValue);
    }
}
