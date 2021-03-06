package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.utils.strings.StringUtils;

public class FieldLengthView extends FieldDialogView<LengthValue> {
    public FieldLengthView(Context context) {
        super(context);
    }

    public FieldLengthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldLengthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_length;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable LengthValue value) {
        return StringUtils.lengthValue(getContext(), value);
    }
}
