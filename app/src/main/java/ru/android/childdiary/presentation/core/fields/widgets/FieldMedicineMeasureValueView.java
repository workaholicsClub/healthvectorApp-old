package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Collections;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.fields.adapters.MedicineMeasureAdapter;
import ru.android.childdiary.utils.StringUtils;

public class FieldMedicineMeasureValueView extends FieldDialogView<MedicineMeasureValue> {
    private final MedicineMeasureAdapter adapter = new MedicineMeasureAdapter(getContext(), Collections.emptyList());

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

    @Override
    protected String getTextForValue(@Nullable MedicineMeasureValue value) {
        return value == null
                ? null
                : StringUtils.medicineMeasureValue(getContext(), value.getAmount(), value.getMedicineMeasure());
    }
}
