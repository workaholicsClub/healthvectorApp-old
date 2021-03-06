package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Breast;
import ru.android.healthvector.utils.strings.StringUtils;

public class FieldBreastView extends FieldRadioView<Breast> {
    public FieldBreastView(Context context) {
        super(context);
    }

    public FieldBreastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldBreastView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Class<Breast> getEnumType() {
        return Breast.class;
    }

    @Override
    @LayoutRes
    protected int getTitleLayoutResourceId() {
        return R.layout.field_breast;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable Breast value) {
        return StringUtils.breast(getContext(), value);
    }
}
