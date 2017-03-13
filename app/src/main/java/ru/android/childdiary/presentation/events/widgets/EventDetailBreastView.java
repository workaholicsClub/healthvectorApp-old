package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailBreastView extends EventDetailRadioView<Breast> {
    public EventDetailBreastView(Context context) {
        super(context);
    }

    public EventDetailBreastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailBreastView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Class<Breast> getEnumType() {
        return Breast.class;
    }

    @Override
    protected int getTitleLayoutResourceId() {
        return R.layout.event_detail_breast;
    }

    @Override
    protected String getTextForValue(Breast value) {
        return StringUtils.breast(getContext(), value);
    }
}
