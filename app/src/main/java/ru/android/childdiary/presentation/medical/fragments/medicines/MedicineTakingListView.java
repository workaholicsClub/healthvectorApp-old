package ru.android.childdiary.presentation.medical.fragments.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface MedicineTakingListView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMedicineTakingList(@NonNull MedicineTakingListFilter filter, @NonNull List<MedicineTaking> medicineTakingList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTaking(@NonNull MedicineTaking medicineTaking, @NonNull MedicineTaking defaultMedicineTaking);
}
