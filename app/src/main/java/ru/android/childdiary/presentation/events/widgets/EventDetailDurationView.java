package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.TimeUtils;

public class EventDetailDurationView extends EventDetailDialogView<Integer> {
    public EventDetailDurationView(Context context) {
        super(context);
    }

    public EventDetailDurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailDurationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getValueInt() {
        return getValue() == null ? 0 : getValue();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.event_detail_duration;
    }

    @Override
    protected String getTextForValue(Integer value) {
        return TimeUtils.duration(getContext(), value);
    }
}
