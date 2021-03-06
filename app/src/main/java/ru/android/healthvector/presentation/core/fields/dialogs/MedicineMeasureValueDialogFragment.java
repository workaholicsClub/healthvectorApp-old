package ru.android.healthvector.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.NumberPicker;

import butterknife.BindView;
import io.reactivex.Observable;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.medical.data.MedicineMeasureValue;
import ru.android.healthvector.presentation.core.BaseMvpDialogFragment;
import ru.android.healthvector.presentation.core.widgets.CustomEditText;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.strings.DoubleUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class MedicineMeasureValueDialogFragment extends BaseMvpDialogFragment<MedicineMeasureValueDialogArguments> {
    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_medicine_measure_value;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        editText.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.setSelection(editText.getText().length());
            } else {
                Double amount = readAmount();
                editText.setText(DoubleUtils.multipleUnitFormat(amount));
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

        MedicineMeasureValue medicineMeasureValue = dialogArguments.getMedicineMeasureValue();
        Double amount = medicineMeasureValue == null ? null : medicineMeasureValue.getAmount();
        MedicineMeasure medicineMeasure = medicineMeasureValue == null ? null : medicineMeasureValue.getMedicineMeasure();
        editText.setText(DoubleUtils.multipleUnitFormat(amount));
        int index = dialogArguments.getMedicineMeasureList().indexOf(medicineMeasure);
        index = index < 0 ? 0 : index;
        numberPicker.setValue(index);
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.medicine_measure)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus();
                            Double amount = readAmount();
                            MedicineMeasure medicineMeasure = readMedicineMeasure();
                            if (listener != null) {
                                MedicineMeasureValue medicineMeasureValue = ObjectUtils.isPositive(amount)
                                        ? MedicineMeasureValue.builder()
                                        .amount(amount)
                                        .medicineMeasure(medicineMeasure)
                                        .build()
                                        : null;
                                listener.onSetMedicineMeasureValue(getTag(), medicineMeasureValue);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus());

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Nullable
    private Double readAmount() {
        try {
            return Double.parseDouble(editText.getText().toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private MedicineMeasure readMedicineMeasure() {
        int index = numberPicker.getValue();
        return dialogArguments.getMedicineMeasureList().get(index);
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
        void onSetMedicineMeasureValue(String tag, @Nullable MedicineMeasureValue medicineMeasureValue);
    }
}
