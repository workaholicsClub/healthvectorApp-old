package ru.android.childdiary.presentation.dictionaries.achievements;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerView;

public interface AchievementPickerView extends BasePickerView<Achievement> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void deletionRestrictedAchievement();
}
