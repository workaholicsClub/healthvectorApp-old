package ru.android.childdiary.presentation.dictionaries.foodmeasure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

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
    public FoodMeasureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new FoodMeasureViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(FoodMeasure oldItem, FoodMeasure newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}