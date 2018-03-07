package ru.android.healthvector.presentation.development.partitions.achievements.adapters;

import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.utils.ObjectUtils;

@Value
public class ConcreteAchievementItem {
    ConcreteAchievement concreteAchievement;
    AchievementType achievementType;
    boolean expanded;

    public ConcreteAchievementItem(@NonNull ConcreteAchievement concreteAchievement) {
        this.concreteAchievement = concreteAchievement;
        this.achievementType = null;
        this.expanded = false;
    }

    public ConcreteAchievementItem(@NonNull AchievementType achievementType, boolean expanded) {
        this.concreteAchievement = null;
        this.achievementType = achievementType;
        this.expanded = expanded;
    }

    public boolean isGroup() {
        return concreteAchievement == null;
    }

    public boolean isChild() {
        return !isGroup();
    }

    public boolean same(@NonNull ConcreteAchievementItem concreteAchievementItem) {
        if (isGroup() && concreteAchievementItem.isGroup()) {
            return achievementType == concreteAchievementItem.achievementType;
        }
        //noinspection SimplifiableIfStatement
        if (isChild() && concreteAchievementItem.isChild()) {
            assert concreteAchievement != null;
            assert concreteAchievementItem.concreteAchievement != null;
            return ObjectUtils.equals(concreteAchievement.getId(), concreteAchievementItem.concreteAchievement.getId());
        }
        return false;
    }
}
