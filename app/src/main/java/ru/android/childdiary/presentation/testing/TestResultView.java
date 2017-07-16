package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;

public interface TestResultView extends BaseView {
    @StateStrategyType(SingleStateStrategy.class)
    void showFinish(@NonNull TestingFinishArguments arguments);
}
