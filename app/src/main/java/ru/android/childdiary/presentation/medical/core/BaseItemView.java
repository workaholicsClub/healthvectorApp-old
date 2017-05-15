package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.core.BaseView;

public interface BaseItemView<T extends Serializable> extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFrequencyList(List<Integer> frequencyList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showPeriodicityList(List<PeriodicityType> periodicityList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setDoctor(@Nullable Doctor doctor);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setMedicine(@Nullable Medicine medicine);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMedicineMeasureValueDialog(@NonNull ArrayList<MedicineMeasure> medicineMeasureList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLengthValueDialog(@NonNull ArrayList<TimeUnit> timeUnits);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showGeneratingEvents(boolean loading);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);
}
