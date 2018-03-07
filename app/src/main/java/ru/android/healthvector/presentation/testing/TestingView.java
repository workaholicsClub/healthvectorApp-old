package ru.android.healthvector.presentation.testing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.BaseView;
import ru.android.healthvector.presentation.testing.fragments.TestingFinishArguments;
import ru.android.healthvector.presentation.testing.fragments.TestingQuestionArguments;
import ru.android.healthvector.presentation.testing.fragments.TestingStartArguments;
import ru.android.healthvector.utils.strings.TimeUtils;

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

    @StateStrategyType(OneExecutionStateStrategy.class)
    void specifyTestParameters(@NonNull Child child, @NonNull Test test);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askWhenThisHappened(@Nullable TimeUtils.Age age);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();
}
