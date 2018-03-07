package ru.android.healthvector.presentation.medical.add.medicines;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.presentation.core.events.BaseAddItemView;

public interface AddDoctorVisitView extends BaseAddItemView<DoctorVisit> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void doctorVisitNameValidated(boolean valid);
}
