package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.joda.time.LocalTime;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class EventDetailTimeView extends EventDetailDialogView<LocalTime> {
    public EventDetailTimeView(Context context) {
        super(context);
    }

    public EventDetailTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.event_detail_time;
    }

    @Override
    protected String getTextForValue(@Nullable LocalTime value) {
        return DateUtils.time(value);
    }
}
