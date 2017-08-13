package ru.android.childdiary.presentation.dictionaries.food;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.dictionaries.food.FoodInteractor;
import ru.android.childdiary.domain.dictionaries.food.data.Food;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class FoodAddPresenter extends BaseAddPresenter<Food, FoodAddView> {
    @Getter
    @Inject
    FoodInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.FOOD_NAME;
    }
}
