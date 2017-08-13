package ru.android.childdiary.presentation.dictionaries.foodmeasure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.presentation.dictionaries.core.BaseItemViewHolder;

public class FoodMeasureViewHolder extends BaseItemViewHolder<FoodMeasure,
        FoodMeasureSwipeActionListener,
        FoodMeasureActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public FoodMeasureViewHolder(View itemView,
                                 @NonNull FoodMeasureActionListener itemActionListener,
                                 @NonNull FoodMeasureSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, FoodMeasure item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
