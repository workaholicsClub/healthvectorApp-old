package ru.android.healthvector.presentation.dictionaries.achievements.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class AchievementAdapter extends SwipeViewAdapter<
        Achievement,
        AchievementViewHolder,
        AchievementSwipeActionListener,
        AchievementActionListener> implements AchievementSwipeActionListener {
    public AchievementAdapter(Context context,
                              @NonNull AchievementActionListener itemActionListener,
                              @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected AchievementViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new AchievementViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Achievement oldItem, Achievement newItem) {
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
