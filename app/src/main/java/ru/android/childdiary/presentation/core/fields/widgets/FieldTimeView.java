package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.joda.time.LocalTime;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class FieldTimeView extends FieldDialogView<LocalTime> {
    public FieldTimeView(Context context) {
        super(context);
    }

    public FieldTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.field_time;
    }

    @Override
    protected String getTextForValue(@Nullable LocalTime value) {
        return DateUtils.time(getContext(), value);
    }
}
