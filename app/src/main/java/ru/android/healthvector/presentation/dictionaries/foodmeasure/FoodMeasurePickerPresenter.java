package ru.android.healthvector.presentation.dictionaries.foodmeasure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.foodmeasure.FoodMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.healthvector.utils.strings.StringUtils;

@InjectViewState
public class FoodMeasurePickerPresenter extends BasePickerPresenter<FoodMeasure, FoodMeasurePickerView> {
    @Getter
    @Inject
    FoodMeasureInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected boolean filter(@NonNull FoodMeasure item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }
}
