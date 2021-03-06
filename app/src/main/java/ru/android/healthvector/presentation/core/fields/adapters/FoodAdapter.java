package ru.android.healthvector.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.food.data.Food;

public class FoodAdapter extends SpinnerItemAdapter<Food, FoodAdapter.ViewHolder> {
    public FoodAdapter(Context context, List<Food> foodList) {
        super(context, foodList);
    }

    @Override
    protected FoodAdapter.ViewHolder createViewHolder(View view) {
        return new FoodAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<Food> {
        public ViewHolder(View view) {
            super(view);
        }

        @Nullable
        @Override
        protected String getTextForValue(Context context, @NonNull Food item) {
            return item == Food.NULL
                    ? context.getString(R.string.other)
                    : item.getName();
        }
    }
}
