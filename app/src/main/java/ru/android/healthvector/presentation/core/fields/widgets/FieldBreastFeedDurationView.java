package ru.android.healthvector.presentation.core.fields.widgets;

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
import ru.android.healthvector.R;
import ru.android.healthvector.utils.strings.TimeUtils;

public class FieldBreastFeedDurationView extends LinearLayout implements View.OnClickListener {
    private TextView textViewDuration;
    private TextView textViewDurationLeft, textViewDurationRight;
    private View textViewDurationLeftWrapper, textViewDurationRightWrapper;
    private ImageView imageViewLeft, imageViewRight;

    @Setter
    private FieldDurationListener fieldDurationListener;
    @Getter
    private Integer durationLeft, durationRight;

    public FieldBreastFeedDurationView(Context context) {
        super(context);
        init();
    }

    public FieldBreastFeedDurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldBreastFeedDurationView(Context context, AttributeSet attrs, int defStyle) {
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

        View viewDuration = inflate(getContext(), R.layout.field_duration_breast_feed, null);
        addView(viewDuration);
        textViewDuration = ButterKnife.findById(viewDuration, R.id.textView);

        View viewLeft = inflate(getContext(), R.layout.field_duration_left, null);
        addView(viewLeft);
        textViewDurationLeftWrapper = viewLeft.findViewById(R.id.textViewWrapper);
        textViewDurationLeftWrapper.setOnClickListener(this);
        textViewDurationLeft = ButterKnife.findById(viewLeft, R.id.textView);
        imageViewLeft = ButterKnife.findById(viewLeft, R.id.imageView);

        View viewRight = inflate(getContext(), R.layout.field_duration_right, null);
        addView(viewRight);
        textViewDurationRightWrapper = viewRight.findViewById(R.id.textViewWrapper);
        textViewDurationRightWrapper.setOnClickListener(this);
        textViewDurationRight = ButterKnife.findById(viewRight, R.id.textView);
        imageViewRight = ButterKnife.findById(viewRight, R.id.imageView);
    }

    public void setLeftDuration(@Nullable Integer value) {
        this.durationLeft = value;
        textViewDurationLeft.setText(TimeUtils.durationShort(getContext(), value));
        updateSum();
    }

    public void setRightDuration(@Nullable Integer value) {
        this.durationRight = value;
        textViewDurationRight.setText(TimeUtils.durationShort(getContext(), value));
        updateSum();
    }

    private void updateSum() {
        int left = getDurationLeftInt();
        int right = getDurationRightInt();
        int sum = left + right;
        textViewDuration.setText(TimeUtils.durationShort(getContext(), sum));
    }

    @Override
    public void onClick(View v) {
        if (v == textViewDurationLeftWrapper) {
            if (fieldDurationListener != null) {
                fieldDurationListener.requestLeftValueChange(this);
            }
        } else if (v == textViewDurationRightWrapper) {
            if (fieldDurationListener != null) {
                fieldDurationListener.requestRightValueChange(this);
            }
        }
    }

    public interface FieldDurationListener {
        void requestLeftValueChange(FieldBreastFeedDurationView view);

        void requestRightValueChange(FieldBreastFeedDurationView view);
    }
}
