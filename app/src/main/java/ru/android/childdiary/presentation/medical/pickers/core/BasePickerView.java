package ru.android.childdiary.presentation.medical.pickers.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.presentation.core.BaseView;

public interface BasePickerView<T> extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showList(@NonNull List<T> list);
}
