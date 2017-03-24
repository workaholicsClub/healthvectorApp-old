package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ListAdapter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.presentation.events.adapters.FeedTypeAdapter;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class EventDetailFeedTypeView extends EventDetailSpinnerView<FeedType> {
    private final FeedTypeAdapter adapter = new FeedTypeAdapter(getContext());
    @BindView(R.id.imageViewIcon)
    ImageView imageView;

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
    protected void valueChanged() {
        imageView.setImageResource(ResourcesUtils.getFeedTypeIcon(getValue()));
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.event_detail_feed_type;
    }

    @Override
    protected String getTextForValue(@Nullable FeedType value) {
        return StringUtils.feedType(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }
}
