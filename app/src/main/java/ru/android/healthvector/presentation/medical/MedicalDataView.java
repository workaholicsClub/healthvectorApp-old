package ru.android.healthvector.presentation.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.presentation.core.AppPartitionView;

public interface MedicalDataView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTakingAdd(@NonNull MedicineTaking defaultMedicineTaking,
                                     @Nullable LocalTime startTime,
                                     @Nullable LocalTime finishTime);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDoctorVisitAdd(@NonNull DoctorVisit defaultDoctorVisit,
                                  @Nullable LocalTime startTime,
                                  @Nullable LocalTime finishTime);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();
}
