package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class ConcreteAchievementAdapter extends SwipeViewAdapter<ConcreteAchievement, ConcreteAchievementViewHolder, ConcreteAchievementSwipeActionListener, ConcreteAchievementActionListener>
        implements ConcreteAchievementSwipeActionListener {
    public ConcreteAchievementAdapter(Context context, @NonNull ConcreteAchievementActionListener itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    public ConcreteAchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.concrete_achievement_item, parent, false);
        return new ConcreteAchievementViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(ConcreteAchievement oldItem, ConcreteAchievement newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
