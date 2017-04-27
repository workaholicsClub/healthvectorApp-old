package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class AddValueDialog extends BaseDialogFragment {
    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.dummy)
    View dummy;

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        init(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_value, null);

        ButterKnife.bind(this, view);

        setupUi();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setView(view)
                .setTitle(getTitle())
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    hideKeyboardAndClearFocus(rootView.findFocus());
                    String text = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(text)) {
                        return;
                    }
                    addValue(text);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> hideKeyboardAndClearFocus(rootView.findFocus()));

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void setupUi() {
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

    public void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(getContext(), view);
        view.clearFocus();
        dummy.requestFocus();
    }

    protected abstract int getMaxLength();

    protected abstract String getTitle();

    protected abstract void addValue(String name);
}
