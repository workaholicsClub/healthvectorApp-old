package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;

public class EventDetailTitleView extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    public EventDetailTitleView(Context context) {
        super(context);
        init();
    }

    public EventDetailTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_title, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setTitle(String text) {
        textView.setText(text);
    }

    public void setTitle(@StringRes int res) {
        textView.setText(getContext().getString(res));
    }
}
