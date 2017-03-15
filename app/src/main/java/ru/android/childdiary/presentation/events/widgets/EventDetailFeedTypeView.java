package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.presentation.events.adapters.FeedTypeAdapter;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailFeedTypeView extends EventDetailSpinnerView<FeedType> {
    private final FeedTypeAdapter adapter = new FeedTypeAdapter(getContext());

    public EventDetailFeedTypeView(Context context) {
        super(context);
    }

    public EventDetailFeedTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailFeedTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.event_detail_feed_type;
    }

    @Override
    protected String getTextForValue(FeedType value) {
        return StringUtils.feedType(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }
}
