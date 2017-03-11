package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ru.android.childdiary.R;

public class EventDetailFeedTypeView extends LinearLayout {
    public EventDetailFeedTypeView(Context context) {
        super(context);
        init();
    }

    public EventDetailFeedTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailFeedTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_feed_type, this);
        // ButterKnife.bind(this);
    }
}
