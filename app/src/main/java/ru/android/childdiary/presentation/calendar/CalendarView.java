package ru.android.childdiary.presentation.calendar;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface CalendarView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDiaperEventAdd(@NonNull DiaperEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToFeedEventAdd(@NonNull FeedEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToOtherEventAdd(@NonNull OtherEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPumpEventAdd(@NonNull PumpEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSleepEventAdd(@NonNull SleepEvent defaultEvent);
}
