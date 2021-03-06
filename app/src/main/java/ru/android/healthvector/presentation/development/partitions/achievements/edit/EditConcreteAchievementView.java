package ru.android.healthvector.presentation.development.partitions.achievements.edit;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.presentation.development.partitions.achievements.core.ConcreteAchievementView;

public interface EditConcreteAchievementView extends ConcreteAchievementView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void updated(@NonNull ConcreteAchievement concreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull ConcreteAchievement concreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDelete(@NonNull ConcreteAchievement concreteAchievement);
}
