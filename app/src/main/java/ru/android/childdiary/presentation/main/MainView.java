package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;

public interface MainView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChildList(@NonNull List<Child> childList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileEdit(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileReview();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDeleteChildConfirmation(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCalendar(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDevelopmentDiary(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToExercises(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicalData(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSettings(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToHelp(@NonNull Child child);
}
