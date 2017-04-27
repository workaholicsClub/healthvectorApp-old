package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.BindDimen;
import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;
import ru.android.childdiary.utils.StringUtils;

public class FeedTypeAdapter extends BaseArrayAdapter<FeedType, FeedTypeAdapter.ViewHolder> {
    public FeedTypeAdapter(Context context) {
        super(context, Arrays.asList(FeedType.values()));
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.spinner_item;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<FeedType> {
        @BindView(android.R.id.text1)
        TextView textView;
        @BindView(R.id.imageViewDropdown)
        View imageViewDropdown;
        @BindDimen(R.dimen.base_margin_horizontal)
        int baseMarginHorizontal;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(Context context, int position, FeedType value) {
            String text = StringUtils.feedType(context, value);
            textView.setText(text);
            imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.rightMargin = position == 0 ? 0 : baseMarginHorizontal;
            textView.setLayoutParams(params);
        }
    }
}
