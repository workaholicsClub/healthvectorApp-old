package ru.android.childdiary.presentation.development;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface DevelopmentDiaryView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAchievementAdd(@NonNull Child child, @NonNull ConcreteAchievement defaultConcreteAchievement);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAntropometryAdd(@NonNull Child child, @NonNull Antropometry defaultAntropometry);
}
