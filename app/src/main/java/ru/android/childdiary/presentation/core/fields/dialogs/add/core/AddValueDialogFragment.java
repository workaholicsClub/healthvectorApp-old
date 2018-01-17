package ru.android.childdiary.presentation.core.fields.dialogs.add.core;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class AddValueDialogFragment<P extends AddValueDialogArguments, T, V extends AddValueView<T>>
        extends BaseMvpDialogFragment<P> implements AddValueView<T> {
    @BindView(R.id.editText)
    CustomEditText editText;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_add_value;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(getMaxLength())});

        editText.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.setSelection(editText.getText().length());
            }
        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus(editText);
                return true;
            }
            return false;
        });
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(getTitle())
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus());

        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener((dialogView) -> {
                hideKeyboardAndClearFocus();
                String name = editText.getText().toString().trim();
                getPresenter().add(buildItem(name));
            });
        });

        return dialog;
    }

    protected abstract int getMaxLength();

    protected abstract String getTitle();

    protected abstract T buildItem(@NonNull String name);

    protected abstract AddValuePresenter<T, V> getPresenter();

    @Override
    public void showValidationErrorMessage(@NonNull String message) {
        showToast(message);
    }
}
