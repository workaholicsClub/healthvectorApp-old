package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.development.testing.data.TestResult;
import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;

public interface TestResultView extends BaseView {
    @StateStrategyType(SingleStateStrategy.class)
    void showFinish(@NonNull TestingFinishArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDeletion(@NonNull TestResult testResult);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull TestResult testResult);
}
