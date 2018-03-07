package ru.android.healthvector.presentation.development.partitions.achievements;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.presentation.development.partitions.core.BaseDevelopmentDiaryView;

public interface ConcreteAchievementsView extends BaseDevelopmentDiaryView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showConcreteAchievementsState(@NonNull ConcreteAchievementsState state);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToConcreteAchievement(@NonNull Child child, @NonNull ConcreteAchievement concreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull ConcreteAchievement concreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDelete(@NonNull ConcreteAchievement concreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToConcreteAchievementAdd(@NonNull Child child, @NonNull ConcreteAchievement defaultConcreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();
}
