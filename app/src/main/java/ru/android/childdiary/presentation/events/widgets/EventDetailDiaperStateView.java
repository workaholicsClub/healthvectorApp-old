package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailDiaperStateView extends EventDetailRadioView<DiaperState> {
    public EventDetailDiaperStateView(Context context) {
        super(context);
    }

    public EventDetailDiaperStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailDiaperStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Class<DiaperState> getEnumType() {
        return DiaperState.class;
    }

    @Override
    @LayoutRes
    protected int getTitleLayoutResourceId() {
        return R.layout.event_detail_diaper_state;
    }

    @Override
    protected String getTextForValue(DiaperState value) {
        return StringUtils.diaperState(getContext(), value);
    }
}
