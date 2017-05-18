package ru.android.childdiary.presentation.medical.partitions.visits;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface DoctorVisitsView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDoctorVisits(@NonNull DoctorVisitsFilter filter, @NonNull List<DoctorVisit> doctorVisits);

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

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);
}
