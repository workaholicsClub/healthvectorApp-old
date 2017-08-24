package ru.android.childdiary.presentation.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionView;

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
}
