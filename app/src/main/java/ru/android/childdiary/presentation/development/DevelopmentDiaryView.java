package ru.android.childdiary.presentation.development;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.AppPartitionView;

public interface DevelopmentDiaryView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAchievementAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToTestResult();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToTest();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAntropometryAdd();
}
