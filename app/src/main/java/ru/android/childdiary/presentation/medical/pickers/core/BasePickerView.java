package ru.android.childdiary.presentation.medical.pickers.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.core.bindings.SearchViewQueryTextEvent;

public interface BasePickerView<T> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void processSearchEvent(@NonNull SearchViewQueryTextEvent event);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showList(@NonNull List<T> list);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void itemDeleted(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deletionRestricted();
}
