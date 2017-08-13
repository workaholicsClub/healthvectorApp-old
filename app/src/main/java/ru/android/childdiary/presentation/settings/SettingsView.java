package ru.android.childdiary.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.presentation.cloud.core.CloudView;

public interface SettingsView extends CloudView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChildList(@NonNull List<Child> childList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSelectedDate(@NonNull LocalDate selectedDate);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSelectedAccount(@NonNull String accountName);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileEdit(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDeleteChildConfirmation(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void childDeleted(@NonNull Child child);
}
