package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailNotificationTimeView extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;
    @Getter
    private int minutes;

    public EventDetailNotificationTimeView(Context context) {
        super(context);
        init();
    }

    public EventDetailNotificationTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailNotificationTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_notification_time, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setMinutes(@Nullable Integer minutes) {
        this.minutes = minutes == null ? 0 : minutes;
        textView.setText(StringUtils.notifyTime(getContext(), minutes));
    }
}
