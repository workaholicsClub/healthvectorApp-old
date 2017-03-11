package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailDiaperStateView extends LinearLayout {
    public EventDetailDiaperStateView(Context context) {
        super(context);
        init();
    }

    public EventDetailDiaperStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailDiaperStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View child;
        child = inflate(getContext(), R.layout.event_detail_diaper_state, null);
        addView(child);
        for (DiaperState diaperState : DiaperState.values()) {
            child = inflate(getContext(), R.layout.event_detail_radio, null);
            addView(child);
            TextView textView = ButterKnife.findById(child, R.id.textView);
            textView.setText(StringUtils.diaperState(getContext(), diaperState));
        }
        // ButterKnife.bind(this);
    }
}
