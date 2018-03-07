package ru.android.healthvector.domain.dictionaries.food;

import javax.inject.Inject;

import ru.android.healthvector.data.repositories.dictionaries.food.FoodDataRepository;
import ru.android.healthvector.domain.dictionaries.core.BaseDictionaryInteractor;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.domain.dictionaries.food.validation.FoodValidator;

public class FoodInteractor extends BaseDictionaryInteractor<Food> {
    @Inject
    public FoodInteractor(FoodDataRepository repository,
                          FoodValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Food buildItem(String name) {
        return Food.builder().nameUser(name).build();
    }
}
