package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class EventDetailDateView extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    @Setter
    private OnDateClickListener onDateClickListener;
    @Getter
    private LocalDate date;

    public EventDetailDateView(Context context) {
        super(context);
        init();
    }

    public EventDetailDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_date, this);
        ButterKnife.bind(this);
    }

    public void setDate(@Nullable LocalDate date) {
        this.date = date;
        textView.setText(DateUtils.date(date));
    }

    @OnClick(R.id.textView)
    void onTextViewClick() {
        if (onDateClickListener != null) {
            onDateClickListener.onDateClick();
        }
    }

    public interface OnDateClickListener {
        void onDateClick();
    }
}
