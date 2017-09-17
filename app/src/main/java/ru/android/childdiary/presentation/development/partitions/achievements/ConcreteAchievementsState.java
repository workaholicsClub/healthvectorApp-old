package ru.android.childdiary.presentation.development.partitions.achievements;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementItem;

@Value
@Builder
public class ConcreteAchievementsState {
    @NonNull
    Child child;
    @NonNull
    List<ConcreteAchievementItem> concreteAchievements;
}
