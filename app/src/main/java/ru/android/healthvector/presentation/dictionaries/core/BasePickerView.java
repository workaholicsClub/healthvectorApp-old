package ru.android.healthvector.presentation.dictionaries.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.healthvector.presentation.core.BaseView;
import ru.android.healthvector.presentation.core.bindings.SearchViewQueryTextEvent;

public interface BasePickerView<T> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void processSearchEvent(@NonNull SearchViewQueryTextEvent event);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showList(@NonNull List<T> list, boolean isFiltering);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateTo(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDelete(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deletionRestricted();
}
