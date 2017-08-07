package ru.android.childdiary.presentation.settings.daylength;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.childdiary.presentation.core.BaseView;

public interface DayLengthView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showStartTime(@NonNull LocalTime time);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFinishTime(@NonNull LocalTime time);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void exit(boolean saved);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmSave(@NonNull LocalTime startTime, @NonNull LocalTime finishTime);
}
