package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import java.util.List;

import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableGroup;

public class AchievementGroup extends ExpandableGroup<ConcreteAchievement> {
    public AchievementGroup(String title, List<ConcreteAchievement> items) {
        super(title, items);
    }
}
