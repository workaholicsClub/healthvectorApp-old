package ru.android.healthvector.domain.dictionaries.food.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.dictionaries.food.FoodDataRepository;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidator;
import ru.android.healthvector.domain.dictionaries.food.data.Food;

public class FoodValidator extends DictionaryValidator<Food> {
    @Inject
    public FoodValidator(Context context, FoodDataRepository repository) {
        super(context, repository);
    }

    @Override
    protected String getName(@NonNull Food item) {
        return item.getName();
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.FOOD_NAME;
    }

    @Override
    protected String getEmptyNameMessage() {
        return context.getString(R.string.enter_food_name);
    }
}
