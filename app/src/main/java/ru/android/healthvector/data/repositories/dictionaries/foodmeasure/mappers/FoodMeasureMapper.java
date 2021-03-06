package ru.android.healthvector.data.repositories.dictionaries.foodmeasure.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.dictionaries.FoodMeasureData;
import ru.android.healthvector.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;

public class FoodMeasureMapper implements EntityMapper<FoodMeasureData, FoodMeasureEntity, FoodMeasure> {
    @Inject
    public FoodMeasureMapper() {
    }

    @Override
    public FoodMeasure mapToPlainObject(@NonNull FoodMeasureData foodMeasureData) {
        return FoodMeasure.builder()
                .id(foodMeasureData.getId())
                .nameEn(foodMeasureData.getNameEn())
                .nameRu(foodMeasureData.getNameRu())
                .nameUser(foodMeasureData.getNameUser())
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
        to.setNameEn(from.getNameEn());
        to.setNameRu(from.getNameRu());
        to.setNameUser(from.getNameUser());
    }
}
