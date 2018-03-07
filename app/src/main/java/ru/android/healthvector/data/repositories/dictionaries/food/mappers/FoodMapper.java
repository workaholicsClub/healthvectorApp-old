package ru.android.healthvector.data.repositories.dictionaries.food.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.dictionaries.FoodData;
import ru.android.healthvector.data.db.entities.dictionaries.FoodEntity;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.dictionaries.food.data.Food;

public class FoodMapper implements EntityMapper<FoodData, FoodEntity, Food> {
    @Inject
    public FoodMapper() {
    }

    @Override
    public Food mapToPlainObject(@NonNull FoodData foodData) {
        return Food.builder()
                .id(foodData.getId())
                .nameEn(foodData.getNameEn())
                .nameRu(foodData.getNameRu())
                .nameUser(foodData.getNameUser())
                .build();
    }

    @Override
    public FoodEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                  @NonNull Food food) {
        FoodEntity foodEntity;
        if (food.getId() == null) {
            foodEntity = new FoodEntity();
        } else {
            foodEntity = (FoodEntity) blockingEntityStore.findByKey(FoodEntity.class, food.getId());
        }
        fillNonReferencedFields(foodEntity, food);
        return foodEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull FoodEntity to, @NonNull Food from) {
        to.setNameEn(from.getNameEn());
        to.setNameRu(from.getNameRu());
        to.setNameUser(from.getNameUser());
    }
}
