package ru.android.healthvector.presentation.dictionaries.food.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.presentation.dictionaries.core.BaseItemViewHolder;

public class FoodViewHolder extends BaseItemViewHolder<Food,
        FoodSwipeActionListener,
        FoodActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public FoodViewHolder(View itemView,
                          @Nullable Sex sex,
                          @NonNull FoodActionListener itemActionListener,
                          @NonNull FoodSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, Food item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
