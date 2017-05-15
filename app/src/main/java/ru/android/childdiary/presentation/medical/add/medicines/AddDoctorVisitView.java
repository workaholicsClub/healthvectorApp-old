package ru.android.childdiary.presentation.medical.add.medicines;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.medical.core.BaseAddItemView;

public interface AddDoctorVisitView extends BaseAddItemView<DoctorVisit> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void doctorVisitNameValidated(boolean valid);
}
