package ru.android.childdiary.presentation.core.dialogs;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.strings.StringUtils;

public abstract class BaseLengthValueDialogFragment<T extends BaseLengthValueDialogArguments> extends BaseMvpDialogFragment<T> {
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.numberPickerLength)
    NumberPicker numberPickerLength;

    @BindView(R.id.numberPickerTimeUnit)
    NumberPicker numberPickerTimeUnit;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_length_value;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        textView.setVisibility(TextUtils.isEmpty(dialogArguments.getDescription()) ? View.GONE : View.VISIBLE);
        textView.setText(dialogArguments.getDescription());

        LengthValue lengthValue = dialogArguments.getLengthValue();
        if (ObjectUtils.isEmpty(lengthValue)) {
            TimeUnit timeUnit = dialogArguments.getTimeUnits().get(0);
            int length = dialogArguments.getTimeUnitValues().get(timeUnit).get(0);
            lengthValue = LengthValue.builder().length(length).timeUnit(timeUnit).build();
        }

        numberPickerLength.setMinValue(0);
        numberPickerTimeUnit.setMinValue(0);
        numberPickerTimeUnit.setMaxValue(dialogArguments.getTimeUnits().size() - 1);

        @SuppressWarnings("ConstantConditions") int defaultLength = lengthValue.getLength();
        TimeUnit defaultTimeUnit = lengthValue.getTimeUnit();

        setLengths(dialogArguments.getTimeUnitValues().get(defaultTimeUnit));
        setTimeUnits(defaultLength, dialogArguments.getTimeUnits());

        int lengthIndex = dialogArguments.getTimeUnitValues().get(defaultTimeUnit).indexOf(defaultLength);
        lengthIndex = lengthIndex < 0 ? 0 : lengthIndex;

        int timeUnitIndex = dialogArguments.getTimeUnits().indexOf(defaultTimeUnit);
        timeUnitIndex = timeUnitIndex < 0 ? 0 : timeUnitIndex;

        numberPickerLength.setValue(lengthIndex);
        numberPickerTimeUnit.setValue(timeUnitIndex);

        numberPickerLength.setOnValueChangedListener((picker, oldVal, newVal) -> {
            TimeUnit timeUnit = readTimeUnit();
            setTimeUnits(readLength(timeUnit), dialogArguments.getTimeUnits());
        });
        numberPickerTimeUnit.setOnValueChangedListener((picker, oldVal, newVal) -> {
            TimeUnit timeUnit = readTimeUnit();
            setLengths(dialogArguments.getTimeUnitValues().get(timeUnit));
            setTimeUnits(readLength(timeUnit), dialogArguments.getTimeUnits());
        });
    }

    private int readLength(TimeUnit timeUnit) {
        int index = numberPickerLength.getValue();
        return dialogArguments.getTimeUnitValues().get(timeUnit).get(index);
    }

    private TimeUnit readTimeUnit() {
        int index = numberPickerTimeUnit.getValue();
        return dialogArguments.getTimeUnits().get(index);
    }

    protected LengthValue readLengthValue() {
        TimeUnit timeUnit = readTimeUnit();
        int length = readLength(timeUnit);
        LengthValue lengthValue = LengthValue.builder()
                .length(length)
                .timeUnit(timeUnit)
                .build();
        return lengthValue;
    }

    private void setLengths(List<Integer> values) {
        String[] names = Observable.fromIterable(values)
                .map(String::valueOf)
                .toList()
                .map(strings -> strings.toArray(new String[strings.size()]))
                .blockingGet();
        if (numberPickerLength.getMaxValue() >= names.length) {
            numberPickerLength.setMaxValue(names.length - 1);
        }
        numberPickerLength.setDisplayedValues(names);
        numberPickerLength.setMaxValue(names.length - 1);
    }

    private void setTimeUnits(int length, List<TimeUnit> timeUnits) {
        String[] names = Observable.fromIterable(timeUnits)
                .map(timeUnit -> StringUtils.timeUnit(getContext(), length, timeUnit))
                .toList()
                .map(strings -> strings.toArray(new String[strings.size()]))
                .blockingGet();
        numberPickerTimeUnit.setDisplayedValues(names);
    }
}
