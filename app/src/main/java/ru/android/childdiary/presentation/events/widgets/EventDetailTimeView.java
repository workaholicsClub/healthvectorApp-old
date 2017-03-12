package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class EventDetailTimeView extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    @Setter
    private OnTimeClickListener onTimeClickListener;
    @Getter
    private LocalTime time;

    public EventDetailTimeView(Context context) {
        super(context);
        init();
    }

    public EventDetailTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_time, this);
        ButterKnife.bind(this);
    }

    public void setTime(@Nullable LocalTime time) {
        this.time = time;
        textView.setText(DateUtils.time(time));
    }

    @OnClick(R.id.textView)
    void onTextViewClick() {
        if (onTimeClickListener != null) {
            onTimeClickListener.onTimeClick();
        }
    }

    public interface OnTimeClickListener {
        void onTimeClick();
    }
}
