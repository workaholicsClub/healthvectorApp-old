package ru.android.healthvector.presentation.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.presentation.events.core.PeriodicEventDetailView;

public interface MedicineTakingEventDetailView extends PeriodicEventDetailView<MedicineTakingEvent> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMedicineMeasureValueDialog(@NonNull List<MedicineMeasure> medicineMeasureList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setMedicine(@Nullable Medicine medicine);
}
