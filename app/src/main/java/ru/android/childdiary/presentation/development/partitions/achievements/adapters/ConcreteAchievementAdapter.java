package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeDeleteAction;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeManager;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableGroup;

public class ConcreteAchievementAdapter extends ExpandableRecyclerViewAdapter<AchievementGroupViewHolder, ConcreteAchievementViewHolder>
        implements ConcreteAchievementSwipeActionListener, SwipeController {
    private final ConcreteAchievementItemActionListener itemActionListener;
    private final SwipeManager swipeManager;
    @Nullable
    private Sex sex;

    public ConcreteAchievementAdapter(Context context,
                                      @NonNull List<? extends ExpandableGroup> groups,
                                      @NonNull ConcreteAchievementItemActionListener itemActionListener,
                                      @Nullable FabController fabController) {
        super(context, groups);
        this.itemActionListener = itemActionListener;
        this.swipeManager = new SwipeManager(fabController);
    }

    public final void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    @Override
    public AchievementGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.achievement_type_item, parent, false);
        return new AchievementGroupViewHolder(view);
    }

    @Override
    public ConcreteAchievementViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.concrete_achievement_item, parent, false);
        return new ConcreteAchievementViewHolder(view, itemActionListener, this);
    }

    @Override
    public void onBindChildViewHolder(ConcreteAchievementViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        ConcreteAchievement concreteAchievement = ((AchievementGroup) group).getItems().get(childIndex);
        holder.bind(context, sex, concreteAchievement);
        swipeManager.bindViewHolder(holder, flatPosition);
    }

    @Override
    public void onBindGroupViewHolder(AchievementGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.bind(context, group.getTitle());
    }

    @Override
    public final void delete(ConcreteAchievementViewHolder viewHolder) {
        viewHolder.getSwipeLayout().addSwipeListener(new SwipeDeleteAction<>(viewHolder.getConcreteAchievement(), itemActionListener));
        swipeManager.closeAllItems();
    }

    @Override
    public void closeAllItems() {
        swipeManager.closeAllItems();
    }

    @Override
    public void setFabController(@Nullable FabController fabController) {
        swipeManager.setFabController(fabController);
    }

    @Override
    public void updateFabState() {
        swipeManager.updateFabState();
    }

    @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        super.onGroupExpanded(positionStart, itemCount);
        if (swipeManager.getOpenPosition() >= positionStart) {
            closeAllItems();
        }
    }

    @Override
    public void onGroupCollapsed(int positionStart, int itemCount) {
        super.onGroupCollapsed(positionStart, itemCount);
        if (swipeManager.getOpenPosition() >= positionStart) {
            closeAllItems();
        }
    }
}
