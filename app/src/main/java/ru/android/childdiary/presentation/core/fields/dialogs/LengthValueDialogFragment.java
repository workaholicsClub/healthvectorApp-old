package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import butterknife.BindView;
import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class LengthValueDialogFragment extends BaseMvpDialogFragment<LengthValueDialogArguments> {
    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.numberPickerLength)
    NumberPicker numberPickerLength;

    @BindView(R.id.numberPickerTimeUnit)
    NumberPicker numberPickerTimeUnit;

    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_length_value;
    }

    @Override
    protected void setupUi() {
        LengthValue lengthValue = dialogArguments.getLengthValue();
        if (ObjectUtils.isEmpty(lengthValue)) {
            lengthValue = LengthValue.builder().length(1).timeUnit(TimeUnit.DAY).build();
        }

        int length = lengthValue.getLength();
        TimeUnit timeUnit = lengthValue.getTimeUnit();

        setMinMax(timeUnit);
        setNames(length);

        int index = dialogArguments.getTimeUnits().indexOf(timeUnit);
        index = index < 0 ? 0 : index;

        numberPickerLength.setValue(length);
        numberPickerTimeUnit.setValue(index);

        numberPickerLength.setOnValueChangedListener((picker, oldVal, newVal) -> setNames(readLength()));

        numberPickerTimeUnit.setOnValueChangedListener((picker, oldVal, newVal) -> {
            setMinMax(readTimeUnit());
            setNames(readLength());
        });
    }

    private int readLength() {
        return numberPickerLength.getValue();
    }

    private TimeUnit readTimeUnit() {
        int index = numberPickerTimeUnit.getValue();
        return dialogArguments.getTimeUnits().get(index);
    }

    private void setNames(int length) {
        String[] names = Observable.fromIterable(dialogArguments.getTimeUnits())
                .map(item -> StringUtils.timeUnit(getContext(), length, item))
                .toList()
                .map(strings -> strings.toArray(new String[strings.size()]))
                .blockingGet();
        numberPickerTimeUnit.setMinValue(0);
        numberPickerTimeUnit.setMaxValue(names.length - 1);
        numberPickerTimeUnit.setDisplayedValues(names);
    }

    private void setMinMax(TimeUnit timeUnit) {
        int minValue = 1;
        int maxValue = 100;
        switch (timeUnit) {
            case DAY:
                maxValue = 30;
                break;
            case WEEK:
                maxValue = 52;
                break;
            case MONTH:
                maxValue = 12;
                break;
        }

        numberPickerLength.setMinValue(minValue);
        numberPickerLength.setMaxValue(maxValue);
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.length)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    hideKeyboardAndClearFocus(rootView.findFocus());
                    int length = readLength();
                    TimeUnit timeUnit = readTimeUnit();
                    if (listener != null) {
                        LengthValue lengthValue = LengthValue.builder()
                                .length(length)
                                .timeUnit(timeUnit)
                                .build();
                        listener.onSetLengthValue(getTag(), lengthValue);
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
        void onSetLengthValue(String tag, @NonNull LengthValue lengthValue);
    }
}
