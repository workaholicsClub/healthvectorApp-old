package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BaseView;

public interface TestingView extends BaseView {
    @StateStrategyType(SingleStateStrategy.class)
    void showStart(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(SingleStateStrategy.class)
    void showQuestion(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(SingleStateStrategy.class)
    void showFinish(@NonNull AppPartitionArguments arguments);
}
