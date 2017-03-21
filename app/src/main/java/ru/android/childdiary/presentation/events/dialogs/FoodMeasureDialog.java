package ru.android.childdiary.presentation.events.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class FoodMeasureDialog extends BaseDialogFragment {
    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.dummy)
    View dummy;

    private Listener listener;

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        init(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_food_measure, null);

        ButterKnife.bind(this, view);

        setupUi();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(sex))
                .setView(view)
                .setTitle(R.string.food_measure_dialog_title)
                .setPositiveButton(R.string.OK, (dialog, which) -> {
                    hideKeyboardAndClearFocus(editText);
                    String text = editText.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        return;
                    }
                    if (listener != null) {
                        FoodMeasure foodMeasure = FoodMeasure.builder().name(text).build();
                        listener.onSetFoodMeasure(getTag(), foodMeasure);
                    }
                })
                .setNegativeButton(R.string.Cancel, null);
        return builder.create();
    }

    private void setupUi() {
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
        void onSetFoodMeasure(String tag, @NonNull FoodMeasure foodMeasure);
    }
}
