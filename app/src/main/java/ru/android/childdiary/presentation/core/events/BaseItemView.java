package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;
import java.util.List;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialog;

public interface BaseItemView<T extends Serializable> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showTimeDialog(String tag, @NonNull Child child, TimeDialog.Parameters parameters);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFrequencyList(List<Integer> frequencyList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showPeriodicityList(List<Integer> periodicityList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLengthList(List<Integer> lengthList);
}
