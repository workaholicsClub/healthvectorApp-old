package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ListAdapter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.presentation.core.fields.adapters.FeedTypeAdapter;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FieldFeedTypeView extends FieldSpinnerView<FeedType> {
    private final FeedTypeAdapter adapter = new FeedTypeAdapter(getContext());

    @BindView(R.id.imageView)
    ImageView imageView;

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
        imageView.setImageResource(ResourcesUtils.getFeedTypeIcon(getValue()));
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
