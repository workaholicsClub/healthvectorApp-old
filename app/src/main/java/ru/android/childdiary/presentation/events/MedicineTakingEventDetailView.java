package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;
import ru.android.childdiary.presentation.events.core.EventDetailView;

public interface MedicineTakingEventDetailView extends EventDetailView<MedicineTakingEvent> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMedicineMeasureValueDialog(@NonNull List<MedicineMeasure> medicineMeasureList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setMedicine(@Nullable Medicine medicine);
}
