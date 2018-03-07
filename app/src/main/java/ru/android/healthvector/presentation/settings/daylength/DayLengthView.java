package ru.android.healthvector.presentation.settings.daylength;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.healthvector.presentation.core.BaseView;

public interface DayLengthView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showStartTime(@NonNull LocalTime time);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showFinishTime(@NonNull LocalTime time);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void exit(boolean saved);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmSave(@NonNull LocalTime startTime, @NonNull LocalTime finishTime);
}
