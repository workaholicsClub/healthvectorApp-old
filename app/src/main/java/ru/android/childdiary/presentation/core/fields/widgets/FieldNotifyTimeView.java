package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.TimeUtils;

public class FieldNotifyTimeView extends FieldDialogView<Integer> {
    public FieldNotifyTimeView(Context context) {
        super(context);
    }

    public FieldNotifyTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldNotifyTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getValueInt() {
        return getValue() == null ? 0 : getValue();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.field_notify_time;
    }

    @Override
    protected String getTextForValue(@Nullable Integer value) {
        return TimeUtils.notifyTime(getContext(), value);
    }
}
