package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class AddValueDialogFragment<T extends AddValueDialogArguments> extends BaseMvpDialogFragment<T> {
    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.editText)
    CustomEditText editText;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_add_value;
    }

    @Override
    protected void setupUi() {
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
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(getTitle())
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus(rootView.findFocus());
                            String text = editText.getText().toString().trim();
                            if (TextUtils.isEmpty(text)) {
                                return;
                            }
                            addValue(text);
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus(rootView.findFocus()));

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    protected abstract int getMaxLength();

    protected abstract String getTitle();

    protected abstract void addValue(String name);
}
