package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ru.android.childdiary.R;

public class EventDetailAmountView extends LinearLayout {
    public EventDetailAmountView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_amount, this);
        // ButterKnife.bind(this);
    }
}
