package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailBreastView extends LinearLayout {
    public EventDetailBreastView(Context context) {
        super(context);
        init();
    }

    public EventDetailBreastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailBreastView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View child;
        child = inflate(getContext(), R.layout.event_detail_breast, null);
        addView(child);
        for (Breast breast : Breast.values()) {
            child = inflate(getContext(), R.layout.event_detail_radio, null);
            addView(child);
            TextView textView = ButterKnife.findById(child, R.id.textView);
            textView.setText(StringUtils.breast(getContext(), breast));
        }
        // ButterKnife.bind(this);
    }
}
