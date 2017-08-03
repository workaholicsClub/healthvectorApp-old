package ru.android.childdiary.presentation.development.partitions.antropometry.edit;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryView;

public interface EditAntropometryView extends AntropometryView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void updated(@NonNull Antropometry antropometry);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull Antropometry antropometry);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDelete(@NonNull Antropometry antropometry);
}
