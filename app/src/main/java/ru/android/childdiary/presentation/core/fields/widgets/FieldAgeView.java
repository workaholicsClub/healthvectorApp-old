package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.strings.TimeUtils;

public class FieldAgeView extends FieldDialogView<TimeUtils.Age> {
    public FieldAgeView(Context context) {
        super(context);
    }

    public FieldAgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldAgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_age;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable TimeUtils.Age value) {
        return TimeUtils.age(getContext(), value);
    }
}
