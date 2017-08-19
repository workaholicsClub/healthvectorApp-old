package ru.android.childdiary.presentation.events;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.presentation.events.core.PeriodicEventDetailView;

public interface DoctorVisitEventDetailView extends PeriodicEventDetailView<DoctorVisitEvent> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setDoctor(@Nullable Doctor doctor);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void doctorVisitEventNameValidated(boolean valid);
}
