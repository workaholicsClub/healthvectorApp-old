package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class EventDetailDateView extends EventDetailDialogView<LocalDate> {
    public EventDetailDateView(Context context) {
        super(context);
    }

    public EventDetailDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.event_detail_date;
    }

    @Override
    protected String getTextForValue(@Nullable LocalDate value) {
        return DateUtils.date(value);
    }
}
