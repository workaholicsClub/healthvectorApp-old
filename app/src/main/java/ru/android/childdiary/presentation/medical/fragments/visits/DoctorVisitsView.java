package ru.android.childdiary.presentation.medical.fragments.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface DoctorVisitsView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDoctorVisits(@NonNull DoctorVisitsFilter filter, @NonNull List<DoctorVisit> doctorVisits);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDoctorVisit(@NonNull DoctorVisit doctorVisit);
}
