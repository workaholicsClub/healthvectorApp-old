package ru.android.childdiary.presentation.dictionaries.achievements.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

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
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new AchievementViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Achievement oldItem, Achievement newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}