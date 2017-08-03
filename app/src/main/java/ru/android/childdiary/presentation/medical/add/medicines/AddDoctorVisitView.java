package ru.android.childdiary.presentation.medical.add.medicines;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;
import ru.android.childdiary.presentation.core.events.BaseAddItemView;

public interface AddDoctorVisitView extends BaseAddItemView<DoctorVisit> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void doctorVisitNameValidated(boolean valid);
}
