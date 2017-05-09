package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.NumberPicker;

import butterknife.BindView;
import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MedicineMeasureValueDialogFragment extends BaseDialogFragment<MedicineMeasureValueDialogArguments> {
    private static final String KEY_PARAMETERS = "parameters";

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_medicine_measure_value;
    }

    @Override
    protected void setupUi() {
        // TODO setup numberpicker, edittext selected value
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

        String[] names = Observable.fromIterable(dialogArguments.getMedicineMeasureList())
                .map(MedicineMeasure::getName)
                .toList()
                .map(strings -> strings.toArray(new String[strings.size()]))
                .blockingGet();
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(names.length - 1);
        numberPicker.setDisplayedValues(names);
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.medicine_measure_dialog_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    hideKeyboardAndClearFocus(rootView.findFocus());
                    // TODO read values
                    if (listener != null) {
                        listener.onSetMedicineMeasure(getTag(), MedicineMeasureValue.builder().build());
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> hideKeyboardAndClearFocus(rootView.findFocus()));

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
        void onSetMedicineMeasure(String tag, @NonNull MedicineMeasureValue medicineMeasureValue);
    }
}
