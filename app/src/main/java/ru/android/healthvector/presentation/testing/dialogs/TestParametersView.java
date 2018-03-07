package ru.android.healthvector.presentation.testing.dialogs;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.processors.core.TestParameters;
import ru.android.healthvector.presentation.core.BaseView;

public interface TestParametersView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void close(@NonNull Child child, @NonNull TestParameters testParameters);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void dateAlreadyUsed(@NonNull LocalDate date, @NonNull TestType testType, @NonNull TestParameters testParameters);
}
