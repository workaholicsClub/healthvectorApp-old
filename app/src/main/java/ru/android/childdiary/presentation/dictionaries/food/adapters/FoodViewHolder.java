package ru.android.childdiary.presentation.dictionaries.food.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.food.data.Food;
import ru.android.childdiary.presentation.dictionaries.core.BaseItemViewHolder;

public class FoodViewHolder extends BaseItemViewHolder<Food,
        FoodSwipeActionListener,
        FoodActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public FoodViewHolder(View itemView,
                          @NonNull FoodActionListener itemActionListener,
                          @NonNull FoodSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
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
