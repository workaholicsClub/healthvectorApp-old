package ru.android.childdiary.domain.interactors.dictionaries.foodmeasure;

import javax.inject.Inject;

import ru.android.childdiary.data.repositories.dictionaries.foodmeasure.FoodMeasureDataRepository;
import ru.android.childdiary.domain.interactors.dictionaries.core.BaseDictionaryInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.validation.FoodMeasureValidator;

public class FoodMeasureInteractor extends BaseDictionaryInteractor<FoodMeasure> {
    @Inject
    public FoodMeasureInteractor(FoodMeasureDataRepository repository,
                                 FoodMeasureValidator validator) {
        super(repository, validator);
    }

    @Override
    protected FoodMeasure buildItem(String name) {
        return FoodMeasure.builder().name(name).build();
    }
}
