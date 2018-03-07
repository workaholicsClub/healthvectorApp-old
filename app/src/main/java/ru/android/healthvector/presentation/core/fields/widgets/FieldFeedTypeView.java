package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ListAdapter;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.FeedType;
import ru.android.healthvector.presentation.core.fields.adapters.FeedTypeAdapter;
import ru.android.healthvector.utils.strings.StringUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class FieldFeedTypeView extends FieldSpinnerView<FeedType> {
    private final FeedTypeAdapter adapter = new FeedTypeAdapter(getContext());

    @BindView(R.id.imageViewFeedType)
    ImageView imageViewFeedType;

    public FieldFeedTypeView(Context context) {
        super(context);
    }

    public FieldFeedTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldFeedTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void valueChanged() {
        super.valueChanged();
        imageViewFeedType.setImageResource(ResourcesUtils.getFeedTypeIcon(getValue()));
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_feed_type;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable FeedType value) {
        return StringUtils.feedType(getContext(), value);
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }
}
