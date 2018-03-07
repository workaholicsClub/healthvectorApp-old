package ru.android.healthvector.presentation.dictionaries.foodmeasure;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.foodmeasure.FoodMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class FoodMeasureAddPresenter extends BaseAddPresenter<FoodMeasure, FoodMeasureAddView> {
    @Getter
    @Inject
    FoodMeasureInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.FOOD_MEASURE_NAME;
    }
}
