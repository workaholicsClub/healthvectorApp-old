package ru.android.childdiary.presentation.dictionaries.foodmeasure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.dictionaries.foodmeasure.FoodMeasureInteractor;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.childdiary.utils.strings.StringUtils;

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
