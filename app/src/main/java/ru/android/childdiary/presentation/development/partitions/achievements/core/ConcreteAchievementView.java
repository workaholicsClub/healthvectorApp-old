package ru.android.childdiary.presentation.development.partitions.achievements.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.presentation.core.BaseView;

public interface ConcreteAchievementView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showAchievements(@NonNull List<Achievement> achievements);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void achievementNameValidated(boolean valid);
}
