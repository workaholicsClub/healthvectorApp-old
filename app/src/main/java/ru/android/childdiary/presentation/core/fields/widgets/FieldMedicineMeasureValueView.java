package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.medical.data.MedicineMeasureValue;
import ru.android.childdiary.utils.strings.StringUtils;

public class FieldMedicineMeasureValueView extends FieldDialogView<MedicineMeasureValue> {
    public FieldMedicineMeasureValueView(Context context) {
        super(context);
    }

    public FieldMedicineMeasureValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldMedicineMeasureValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_medicine_measure;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable MedicineMeasureValue value) {
        return StringUtils.medicineMeasureValue(getContext(), value);
    }
}
