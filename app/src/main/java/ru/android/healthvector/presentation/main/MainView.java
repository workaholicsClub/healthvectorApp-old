package ru.android.healthvector.presentation.main;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.presentation.core.AppPartitionArguments;
import ru.android.healthvector.presentation.core.BaseView;

public interface MainView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingProfile(boolean loading);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChildList(@NonNull List<Child> childList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAddFirstTime();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileEdit(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDeleteChildConfirmation(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void childDeleted(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCalendar(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDevelopmentDiary(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToExercises(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicalData(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSettings(@NonNull AppPartitionArguments arguments);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToHelp(@NonNull AppPartitionArguments arguments);
}
