package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class FieldDateView extends FieldDialogView<LocalDate> {
    public FieldDateView(Context context) {
        super(context);
    }

    public FieldDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_date;
    }

    @Override
    protected String getTextForValue(@Nullable LocalDate value) {
        return DateUtils.date(getContext(), value);
    }
}
