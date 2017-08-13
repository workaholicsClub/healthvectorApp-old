package ru.android.childdiary.presentation.medical.partitions.visits;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorVisitFilterDialogArguments;
import ru.android.childdiary.presentation.medical.partitions.core.BaseMedicalDataView;

public interface DoctorVisitsView extends BaseMedicalDataView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showFilterDialog(@NonNull DoctorVisitFilterDialogArguments dialogArguments);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDoctorVisitsState(@NonNull DoctorVisitsState doctorVisitsState);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDoctorVisit(@NonNull DoctorVisit doctorVisit, @NonNull DoctorVisit defaultDoctorVisit,
                               @Nullable LocalTime startTime,
                               @Nullable LocalTime finishTime);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void doctorVisitDeleted(@NonNull DoctorVisit doctorVisit);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDeleteDoctorVisit(@NonNull DoctorVisit item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askDeleteConnectedEventsOrNot(@NonNull DoctorVisit doctorVisit);
}
