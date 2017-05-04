package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.TimeUtils;

public class FieldDurationView extends FieldDialogView<Integer> {
    public FieldDurationView(Context context) {
        super(context);
    }

    public FieldDurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDurationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getValueInt() {
        return getValue() == null ? 0 : getValue();
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_duration;
    }

    @Override
    protected String getTextForValue(@Nullable Integer value) {
        return TimeUtils.durationShort(getContext(), value);
    }
}
