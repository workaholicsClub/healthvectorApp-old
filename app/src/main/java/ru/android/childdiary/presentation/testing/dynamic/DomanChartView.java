package ru.android.childdiary.presentation.testing.dynamic;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.LinkedHashMap;
import java.util.List;

import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.presentation.core.BaseView;

public interface DomanChartView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showResults(@NonNull LinkedHashMap<DomanTestParameter, List<DomanResult>> results);
}
