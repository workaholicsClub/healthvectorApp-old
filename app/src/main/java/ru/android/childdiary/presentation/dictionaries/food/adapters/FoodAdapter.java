package ru.android.childdiary.presentation.dictionaries.food.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.dictionaries.food.data.Food;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class FoodAdapter extends SwipeViewAdapter<
        Food,
        FoodViewHolder,
        FoodSwipeActionListener,
        FoodActionListener> implements FoodSwipeActionListener {
    public FoodAdapter(Context context,
                       @NonNull FoodActionListener itemActionListener,
                       @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected FoodViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new FoodViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Food oldItem, Food newItem) {
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
