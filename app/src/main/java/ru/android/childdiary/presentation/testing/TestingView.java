package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingStartArguments;

public interface TestingView extends BaseView {
    @StateStrategyType(SingleStateStrategy.class)
    void showStart(@NonNull TestingStartArguments arguments);

    @StateStrategyType(SingleStateStrategy.class)
    void showQuestion(@NonNull TestingQuestionArguments arguments);

    @StateStrategyType(SingleStateStrategy.class)
    void showFinish(@NonNull TestingFinishArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCloseConfirmation();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();
}
