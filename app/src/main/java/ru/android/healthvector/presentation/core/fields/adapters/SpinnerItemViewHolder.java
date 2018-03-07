package ru.android.healthvector.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.adapters.BaseViewHolder;

abstract class SpinnerItemViewHolder<T> extends BaseViewHolder<T> {
    @BindView(android.R.id.text1)
    TextView textView;
    @BindView(R.id.imageViewDropdown)
    View imageViewDropdown;
    @BindDimen(R.dimen.base_margin)
    int baseMarginHorizontal;

    public SpinnerItemViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Context context, int position, T item) {
        String text = getTextForValue(context, item);
        textView.setText(text);
        imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
        params.rightMargin = position == 0 ? 0 : baseMarginHorizontal;
        textView.setLayoutParams(params);
    }

    @Nullable
    protected abstract String getTextForValue(Context context, @NonNull T item);
}
