package ru.android.childdiary.presentation.medical.partitions.medicines;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.presentation.medical.filter.medicines.MedicineTakingFilterDialogArguments;
import ru.android.childdiary.presentation.medical.partitions.core.BaseMedicalDataView;

public interface MedicineTakingListView extends BaseMedicalDataView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showFilterDialog(@NonNull MedicineTakingFilterDialogArguments dialogArguments);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMedicineTakingListState(@NonNull MedicineTakingListState medicineTakingListState);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTaking(@NonNull MedicineTaking medicineTaking, @NonNull MedicineTaking defaultMedicineTaking,
                                  @Nullable LocalTime startTime,
                                  @Nullable LocalTime finishTime);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void medicineTakingDeleted(@NonNull MedicineTaking medicineTaking);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDeleteMedicineTaking(@NonNull MedicineTaking medicineTaking);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askDeleteConnectedEventsOrNot(@NonNull MedicineTaking medicineTaking);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTakingAdd(@NonNull MedicineTaking defaultMedicineTaking,
                                     @Nullable LocalTime startTime,
                                     @Nullable LocalTime finishTime);
}
