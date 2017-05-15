package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;

import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.events.core.EventDetailView;

public interface MedicineTakingEventDetailView extends EventDetailView<MedicineTakingEvent> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMedicineMeasureValueDialog(@NonNull ArrayList<MedicineMeasure> medicineMeasureList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setMedicine(@Nullable Medicine medicine);
}
