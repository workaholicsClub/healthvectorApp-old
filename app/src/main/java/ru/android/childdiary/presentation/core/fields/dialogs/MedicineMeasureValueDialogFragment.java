package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.NumberPicker;

import butterknife.BindView;
import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.data.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.strings.DoubleUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MedicineMeasureValueDialogFragment extends BaseMvpDialogFragment<MedicineMeasureValueDialogArguments> {
    @BindView(R.id.rootView)
    View rootView;

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
    protected void setupUi() {
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
    protected Dialog createDialog(@Nullable View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.medicine_measure_value_dialog_title)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus(rootView.findFocus());
                            Double amount = readAmount();
                            MedicineMeasure medicineMeasure = readMedicineMeasure();
                            if (listener != null) {
                                MedicineMeasureValue medicineMeasureValue = MedicineMeasureValue.builder()
                                        .amount(amount)
                                        .medicineMeasure(medicineMeasure)
                                        .build();
                                listener.onSetMedicineMeasureValue(getTag(), medicineMeasureValue);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus(rootView.findFocus()));

        return builder.setCancelable(false)
                .create();
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
        void onSetMedicineMeasureValue(String tag, @NonNull MedicineMeasureValue medicineMeasureValue);
    }
}
