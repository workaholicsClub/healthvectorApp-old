package ru.android.healthvector.presentation.development.partitions.achievements.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.presentation.development.partitions.achievements.core.ConcreteAchievementView;

public interface AddConcreteAchievementView extends ConcreteAchievementView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull ConcreteAchievement concreteAchievement);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);
}
