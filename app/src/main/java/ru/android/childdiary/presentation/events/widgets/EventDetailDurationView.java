package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;

public class EventDetailDurationView extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    public EventDetailDurationView(Context context) {
        super(context);
        init();
    }

    public EventDetailDurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailDurationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_duration, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
