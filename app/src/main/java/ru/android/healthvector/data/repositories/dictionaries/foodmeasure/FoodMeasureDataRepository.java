package ru.android.healthvector.data.repositories.dictionaries.foodmeasure;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.healthvector.data.repositories.dictionaries.core.CrudDataRepository;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;

@Singleton
public class FoodMeasureDataRepository extends CrudDataRepository<FoodMeasure> {
    @Inject
    public FoodMeasureDataRepository(FoodMeasureDbService dbService) {
        super(dbService);
    }
}
