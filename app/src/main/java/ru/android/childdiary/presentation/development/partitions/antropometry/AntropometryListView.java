package ru.android.childdiary.presentation.development.partitions.antropometry;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryView;

public interface AntropometryListView extends BaseDevelopmentDiaryView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showAntropometryListState(@NonNull AntropometryListState state);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAntropometry(@NonNull Child child, @NonNull Antropometry antropometry);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToChart(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChartData();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull Antropometry antropometry);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDelete(@NonNull Antropometry antropometry);
}
