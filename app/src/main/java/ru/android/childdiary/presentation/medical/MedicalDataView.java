package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.AppPartitionView;

public interface MedicalDataView extends AppPartitionView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTakingAdd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDoctorVisitAdd();
}
