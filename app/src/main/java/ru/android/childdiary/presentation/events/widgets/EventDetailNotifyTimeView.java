package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.TimeUtils;

public class EventDetailNotifyTimeView extends EventDetailDialogView<Integer> {
    public EventDetailNotifyTimeView(Context context) {
        super(context);
    }

    public EventDetailNotifyTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailNotifyTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getValueInt() {
        return getValue() == null ? 0 : getValue();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.event_detail_notify_time;
    }

    @Override
    protected String getTextForValue(Integer value) {
        return TimeUtils.notifyTime(getContext(), value);
    }
}
