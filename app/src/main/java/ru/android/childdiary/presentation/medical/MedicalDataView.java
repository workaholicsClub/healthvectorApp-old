package ru.android.childdiary.presentation.medical;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface MedicalDataView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTakingAdd(@NonNull MedicineTaking defaultMedicineTaking);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDoctorVisitAdd(@NonNull DoctorVisit defaultDoctorVisit);
}
