package ru.android.childdiary.presentation.calendar;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.AppPartitionView;

public interface CalendarView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDiaperEventAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToFeedEventAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToOtherEventAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPumpEventAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSleepEventAdd();
}
