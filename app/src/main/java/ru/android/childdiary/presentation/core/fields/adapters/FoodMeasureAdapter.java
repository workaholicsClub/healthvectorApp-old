package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;

public class FoodMeasureAdapter extends SpinnerItemAdapter<FoodMeasure, FoodMeasureAdapter.ViewHolder> {
    public FoodMeasureAdapter(Context context, List<FoodMeasure> foodMeasureList) {
        super(context, foodMeasureList);
    }

    @Override
    protected FoodMeasureAdapter.ViewHolder createViewHolder(View view) {
        return new FoodMeasureAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<FoodMeasure> {
        public ViewHolder(View view) {
            super(view);
        }

        @Nullable
        @Override
        protected String getTextForValue(Context context, @NonNull FoodMeasure item) {
            return item == FoodMeasure.NULL
                    ? context.getString(R.string.food_measure_other)
                    : item.getName();
        }
    }
}
