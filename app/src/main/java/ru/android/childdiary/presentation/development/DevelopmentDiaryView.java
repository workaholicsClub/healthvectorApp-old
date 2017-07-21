package ru.android.childdiary.presentation.development;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface DevelopmentDiaryView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAchievementAdd(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAntropometryAdd(@NonNull Child child);
}
