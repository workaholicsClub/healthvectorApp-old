package ru.android.childdiary.presentation.medical;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.BaseView;

public interface AddMedicineTakingView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMedicines(List<Medicine> medicines);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void medicineTakingAdded(@NonNull MedicineTaking medicineTaking);
}
