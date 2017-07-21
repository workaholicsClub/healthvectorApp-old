package ru.android.childdiary.presentation.development.partitions.antropometry.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryView;

public interface AddAntropometryView extends AntropometryView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull Antropometry antropometry);
}