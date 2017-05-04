package ru.android.childdiary.presentation.medical.edit.visits;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.core.events.BaseEditItemView;

public interface EditMedicineTakingView extends BaseEditItemView<MedicineTaking> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMedicineMeasureList(List<MedicineMeasure> medicineMeasureList);
}
