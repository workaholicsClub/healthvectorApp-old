package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.utils.strings.TestUtils;

public class FieldDomanTestParameterView extends FieldRadioView<DomanTestParameter> {
    public FieldDomanTestParameterView(Context context) {
        super(context);
    }

    public FieldDomanTestParameterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDomanTestParameterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Class<DomanTestParameter> getEnumType() {
        return DomanTestParameter.class;
    }

    @Override
    @LayoutRes
    protected int getTitleLayoutResourceId() {
        return R.layout.field_doman_test_parameter;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable DomanTestParameter value) {
        return TestUtils.testParameter(getContext(), value);
    }
}
