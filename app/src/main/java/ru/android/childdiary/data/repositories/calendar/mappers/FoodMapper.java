package ru.android.childdiary.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.childdiary.data.entities.calendar.events.core.FoodData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodEntity;
import ru.android.childdiary.data.repositories.core.mappers.EntityMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;

public class FoodMapper implements EntityMapper<FoodData, FoodEntity, Food> {
    @Inject
    public FoodMapper() {
    }

    @Override
    public Food mapToPlainObject(@NonNull FoodData data) {
        return Food.builder()
                .id(data.getId())
                .name(data.getName())
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
        to.setName(from.getName());
    }
}
