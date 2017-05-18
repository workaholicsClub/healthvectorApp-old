package ru.android.childdiary.presentation.medical.partitions.medicines;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface MedicineTakingListView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMedicineTakingList(@NonNull MedicineTakingListFilter filter, @NonNull List<MedicineTaking> medicineTakingList);

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

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);
}
