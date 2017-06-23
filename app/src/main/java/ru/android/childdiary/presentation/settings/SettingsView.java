package ru.android.childdiary.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.cloud.core.CloudView;

public interface SettingsView extends CloudView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSelectedDate(@NonNull LocalDate selectedDate);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);
}
