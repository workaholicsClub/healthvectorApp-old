package ru.android.childdiary.presentation.core.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

/**
 * Использовать этот класс вместо стандартных AlertDialog, чтобы сохранять диалог при пересоздании
 * окна (при необходимости). Добавить недостающую функциональность (например, обработку события
 * нажатия Назад и т.п.) при необходимости.
 */
public class AlertDialogFragment extends BaseMvpDialogFragment<AlertDialogArguments> {
    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void setupUi() {
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setTitle(dialogArguments.getTitle())
                .setMessage(dialogArguments.getMessage())
                .setPositiveButton(dialogArguments.getPositiveButtonText(),
                        (dialog, which) -> {
                            if (listener != null) {
                                listener.onPositiveButtonClick(getTag());
                            }
                        })
                .setNegativeButton(dialogArguments.getNegativeButtonText(),
                        (dialog, which) -> {
                            if (listener != null) {
                                listener.onNegativeButtonClick(getTag());
                            }
                        });

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
        void onPositiveButtonClick(String tag);

        void onNegativeButtonClick(String tag);
    }
}
