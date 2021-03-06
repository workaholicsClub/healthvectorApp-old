package ru.android.healthvector.presentation.dictionaries.food;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.food.FoodInteractor;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.healthvector.utils.strings.StringUtils;

@InjectViewState
public class FoodPickerPresenter extends BasePickerPresenter<Food, FoodPickerView> {
    @Getter
    @Inject
    FoodInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected boolean filter(@NonNull Food item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }
}
