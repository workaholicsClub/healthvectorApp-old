package ru.android.healthvector.presentation.development.partitions.antropometry.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.presentation.development.partitions.antropometry.core.AntropometryView;

public interface AddAntropometryView extends AntropometryView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull Antropometry antropometry);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);
}
