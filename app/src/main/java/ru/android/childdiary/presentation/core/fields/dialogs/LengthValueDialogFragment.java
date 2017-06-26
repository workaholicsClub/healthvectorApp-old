package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.presentation.core.dialogs.BaseLengthValueDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class LengthValueDialogFragment extends BaseLengthValueDialogFragment<LengthValueDialogArguments> {
    @Nullable
    private Listener listener;

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.length)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus();
                            if (listener != null) {
                                listener.onSetLengthValue(getTag(), readLengthValue());
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus());

        AlertDialog dialog = builder.create();
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
        void onSetLengthValue(String tag, @NonNull LengthValue lengthValue);
    }
}
