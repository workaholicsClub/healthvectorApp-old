package ru.android.healthvector.presentation.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;
import ru.android.healthvector.domain.calendar.data.core.TimeUnit;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.presentation.core.BaseView;

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
    void showMedicineMeasureValueDialog(@NonNull List<MedicineMeasure> medicineMeasureList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLengthValueDialog(@NonNull Map<TimeUnit, List<Integer>> timeUnitValues);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showGeneratingEvents(boolean loading);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);
}
