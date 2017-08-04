package ru.android.childdiary.data.repositories.dictionaries.food;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.food.data.Food;

@Singleton
public class FoodDataRepository extends CrudDataRepository<Food> {
    @Inject
    public FoodDataRepository(FoodDbService dbService) {
        super(dbService);
    }
}
