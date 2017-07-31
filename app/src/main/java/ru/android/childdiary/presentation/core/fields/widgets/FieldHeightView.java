package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;

import ru.android.childdiary.presentation.core.widgets.RegExpInputFilter;
import ru.android.childdiary.utils.strings.DoubleUtils;

public class FieldHeightView extends FieldUnitView {
    public FieldHeightView(Context context) {
        super(context);
    }

    public FieldHeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldHeightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected InputFilter getInputFilter() {
        return new RegExpInputFilter.SimpleHeightInputFilter();
    }

    @Override
    protected String editString(@Nullable Double value) {
        return DoubleUtils.heightEdit(value);
    }

    @Override
    protected String reviewString(@Nullable Double value) {
        return DoubleUtils.heightReview(getContext(), value);
    }
}
