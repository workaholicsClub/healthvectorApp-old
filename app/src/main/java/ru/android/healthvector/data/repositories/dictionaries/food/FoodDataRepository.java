package ru.android.healthvector.data.repositories.dictionaries.food;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.healthvector.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.healthvector.domain.dictionaries.food.data.Food;

@Singleton
public class FoodDataRepository extends CrudDataRepository<Food> {
    @Inject
    public FoodDataRepository(FoodDbService dbService) {
        super(dbService);
    }
}
