package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.TimeUtils;

public class EventDetailBreastFeedDurationView extends LinearLayout implements View.OnClickListener, ReadOnlyView {
    private TextView textViewDuration;
    private TextView textViewDurationLeft, textViewDurationRight;
    private View textViewDurationLeftWrapper, textViewDurationRightWrapper;
    private ImageView imageViewLeft, imageViewRight;

    @Setter
    private EventDetailDurationListener eventDetailDurationListener;
    @Getter
    private Integer durationLeft, durationRight;

    public EventDetailBreastFeedDurationView(Context context) {
        super(context);
        init();
    }

    public EventDetailBreastFeedDurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailBreastFeedDurationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public int getDurationLeftInt() {
        return durationLeft == null ? 0 : durationLeft;
    }

    public int getDurationRightInt() {
        return durationRight == null ? 0 : durationRight;
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View view;

        view = inflate(getContext(), R.layout.event_detail_duration_breast_feed, null);
        addView(view);
        textViewDuration = ButterKnife.findById(view, R.id.textView);

        view = inflate(getContext(), R.layout.event_detail_duration_left, null);
        addView(view);
        textViewDurationLeftWrapper = ButterKnife.findById(view, R.id.textViewWrapper);
        textViewDurationLeftWrapper.setOnClickListener(this);
        textViewDurationLeft = ButterKnife.findById(view, R.id.textView);
        imageViewLeft = ButterKnife.findById(view, R.id.imageView);

        view = inflate(getContext(), R.layout.event_detail_duration_right, null);
        addView(view);
        textViewDurationRightWrapper = ButterKnife.findById(view, R.id.textViewWrapper);
        textViewDurationRightWrapper.setOnClickListener(this);
        textViewDurationRight = ButterKnife.findById(view, R.id.textView);
        imageViewRight = ButterKnife.findById(view, R.id.imageView);
    }

    public void setLeftDuration(@Nullable Integer value) {
        this.durationLeft = value;
        textViewDurationLeft.setText(TimeUtils.duration(getContext(), value));
        updateSum();
    }

    public void setRightDuration(@Nullable Integer value) {
        this.durationRight = value;
        textViewDurationRight.setText(TimeUtils.duration(getContext(), value));
        updateSum();
    }

    private void updateSum() {
        int left = getDurationLeftInt();
        int right = getDurationRightInt();
        int sum = left + right;
        textViewDuration.setText(TimeUtils.duration(getContext(), sum));
    }

    @Override
    public void onClick(View v) {
        if (v == textViewDurationLeftWrapper) {
            if (eventDetailDurationListener != null) {
                eventDetailDurationListener.requestLeftValueChange(this);
            }
        } else if (v == textViewDurationRightWrapper) {
            if (eventDetailDurationListener != null) {
                eventDetailDurationListener.requestRightValueChange(this);
            }
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        textViewDurationLeftWrapper.setOnClickListener(readOnly ? null : this);
        textViewDurationRightWrapper.setOnClickListener(readOnly ? null : this);
        textViewDurationLeftWrapper.setBackgroundResource(readOnly ? 0 : R.drawable.background_clickable);
        textViewDurationRightWrapper.setBackgroundResource(readOnly ? 0 : R.drawable.background_clickable);
        imageViewLeft.setVisibility(readOnly ? INVISIBLE : VISIBLE);
        imageViewRight.setVisibility(readOnly ? INVISIBLE : VISIBLE);
    }

    public interface EventDetailDurationListener {
        void requestLeftValueChange(EventDetailBreastFeedDurationView view);

        void requestRightValueChange(EventDetailBreastFeedDurationView view);
    }
}
