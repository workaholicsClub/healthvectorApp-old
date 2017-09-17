package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;

public class ConcreteAchievementAdapter extends SwipeViewAdapter<
        ConcreteAchievementItem,
        ConcreteAchievementViewHolder,
        ConcreteAchievementSwipeActionListener,
        ConcreteAchievementItemActionListener> implements ConcreteAchievementSwipeActionListener {
    private static final int GROUP = 1;
    private static final int CHILD = 2;

    public ConcreteAchievementAdapter(Context context,
                                      @NonNull ConcreteAchievementItemActionListener itemActionListener,
                                      @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    public boolean areItemsTheSame(ConcreteAchievementItem oldItem, ConcreteAchievementItem newItem) {
        return oldItem.same(newItem);
    }

    @Override
    protected int getUserViewType(int position) {
        ConcreteAchievementItem concreteAchievementItem = items.get(position);
        return concreteAchievementItem.isGroup() ? GROUP : CHILD;
    }

    @Override
    protected ConcreteAchievementViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case GROUP:
                v = inflater.inflate(R.layout.achievement_type_item, parent, false);
                return new ConcreteAchievementViewHolder(v, itemActionListener, this);
            case CHILD:
                v = inflater.inflate(R.layout.concrete_achievement_item, parent, false);
                return new ConcreteAchievementViewHolder(v, itemActionListener, this);
            default:
                throw new IllegalArgumentException("Unsupported concrete achievement item type");
        }
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
