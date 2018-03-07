package ru.android.healthvector.presentation.dictionaries.foodmeasure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.dictionaries.core.BaseItemViewHolder;

public class FoodMeasureViewHolder extends BaseItemViewHolder<FoodMeasure,
        FoodMeasureSwipeActionListener,
        FoodMeasureActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public FoodMeasureViewHolder(View itemView,
                                 @Nullable Sex sex,
                                 @NonNull FoodMeasureActionListener itemActionListener,
                                 @NonNull FoodMeasureSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
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
