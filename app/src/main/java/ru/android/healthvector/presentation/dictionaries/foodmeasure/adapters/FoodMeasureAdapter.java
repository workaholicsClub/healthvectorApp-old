package ru.android.healthvector.presentation.dictionaries.foodmeasure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class FoodMeasureAdapter extends SwipeViewAdapter<
        FoodMeasure,
        FoodMeasureViewHolder,
        FoodMeasureSwipeActionListener,
        FoodMeasureActionListener> implements FoodMeasureSwipeActionListener {
    public FoodMeasureAdapter(Context context,
                              @NonNull FoodMeasureActionListener itemActionListener,
                              @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected FoodMeasureViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new FoodMeasureViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(FoodMeasure oldItem, FoodMeasure newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean paintDividers() {
        return true;
    }

    @Override
    public boolean useFooter() {
        return true;
    }
}
