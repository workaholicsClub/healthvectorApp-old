package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailDurationView extends LinearLayout implements View.OnClickListener {
    private TextView textViewDuration;
    private TextView textViewDurationLeft;
    private TextView textViewDurationRight;

    @Setter
    private EventDetailDurationListener eventDetailDurationListener;
    @Getter
    private Integer durationLeft, durationRight;

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
        setOrientation(LinearLayout.VERTICAL);
        View view;
        view = inflate(getContext(), R.layout.event_detail_duration, null);
        addView(view);
        textViewDuration = ButterKnife.findById(view, R.id.editText);
        view = inflate(getContext(), R.layout.event_detail_duration_left, null);
        addView(view);
        textViewDurationLeft = ButterKnife.findById(view, R.id.editText);
        textViewDurationLeft.setOnClickListener(this);
        view = inflate(getContext(), R.layout.event_detail_duration_right, null);
        addView(view);
        textViewDurationRight = ButterKnife.findById(view, R.id.editText);
        textViewDurationRight.setOnClickListener(this);
    }

    public void setLeftDuration(@Nullable Integer value) {
        this.durationLeft = value;
        textViewDurationLeft.setText(StringUtils.duration(getContext(), value));
    }

    public void setRightDuration(@Nullable Integer value) {
        this.durationLeft = value;
        textViewDurationLeft.setText(StringUtils.duration(getContext(), value));
    }

    @Override
    public void onClick(View v) {
        if (v == textViewDurationLeft) {
            if (eventDetailDurationListener != null) {
                eventDetailDurationListener.requestLeftValueChange(this);
            }
        } else if (v == textViewDurationRight) {
            if (eventDetailDurationListener != null) {
                eventDetailDurationListener.requestRightValueChange(this);
            }
        }
    }

    public interface EventDetailDurationListener {
        void requestLeftValueChange(EventDetailDurationView view);

        void requestRightValueChange(EventDetailDurationView view);
    }
}
