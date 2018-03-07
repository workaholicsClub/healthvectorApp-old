package ru.android.healthvector.domain.dictionaries.foodmeasure.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.dictionaries.foodmeasure.FoodMeasureDataRepository;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidator;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;

public class FoodMeasureValidator extends DictionaryValidator<FoodMeasure> {
    @Inject
    public FoodMeasureValidator(Context context, FoodMeasureDataRepository repository) {
        super(context, repository);
    }

    @Override
    protected String getName(@NonNull FoodMeasure item) {
        return item.getName();
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.FOOD_MEASURE_NAME;
    }

    @Override
    protected String getEmptyNameMessage() {
        return context.getString(R.string.enter_measure_name);
    }
}
