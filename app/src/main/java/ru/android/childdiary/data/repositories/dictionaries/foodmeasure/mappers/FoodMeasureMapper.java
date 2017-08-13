package ru.android.childdiary.data.repositories.dictionaries.foodmeasure.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureData;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;

public class FoodMeasureMapper implements EntityMapper<FoodMeasureData, FoodMeasureEntity, FoodMeasure> {
    @Inject
    public FoodMeasureMapper() {
    }

    @Override
    public FoodMeasure mapToPlainObject(@NonNull FoodMeasureData foodMeasureData) {
        return FoodMeasure.builder()
                .id(foodMeasureData.getId())
                .name(foodMeasureData.getName())
                .build();
    }

    @Override
    public FoodMeasureEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                         @NonNull FoodMeasure foodMeasure) {
        FoodMeasureEntity foodMeasureEntity;
        if (foodMeasure.getId() == null) {
            foodMeasureEntity = new FoodMeasureEntity();
        } else {
            foodMeasureEntity = (FoodMeasureEntity) blockingEntityStore.findByKey(FoodMeasureEntity.class, foodMeasure.getId());
        }
        fillNonReferencedFields(foodMeasureEntity, foodMeasure);
        return foodMeasureEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull FoodMeasureEntity to, @NonNull FoodMeasure from) {
        to.setName(from.getName());
    }
}
