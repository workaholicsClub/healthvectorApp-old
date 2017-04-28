package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.BaseView;

public interface AddDoctorVisitView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDoctors(List<Doctor> doctors);
}
