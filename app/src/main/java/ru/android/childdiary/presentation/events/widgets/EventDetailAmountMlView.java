package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ru.android.childdiary.R;

public class EventDetailAmountMlView extends LinearLayout {
    public EventDetailAmountMlView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountMlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountMlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_amount_ml, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // ButterKnife.bind(this);
    }
}
