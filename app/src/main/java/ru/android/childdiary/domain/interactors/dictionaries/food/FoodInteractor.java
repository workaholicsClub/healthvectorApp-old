package ru.android.childdiary.domain.interactors.dictionaries.food;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.dictionaries.food.FoodDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.core.BaseDictionaryInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.food.data.Food;
import ru.android.childdiary.domain.interactors.dictionaries.food.validation.FoodValidator;

public class FoodInteractor extends BaseDictionaryInteractor<Food> {
    @Inject
    public FoodInteractor(FoodDataRepository repository,
                          FoodValidator validator) {
        super(repository, validator);
    }

    @Override
    protected Food buildItem(String name) {
        return Food.builder().name(name).build();
    }
}
