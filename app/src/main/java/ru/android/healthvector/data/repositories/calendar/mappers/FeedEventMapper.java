package ru.android.healthvector.data.repositories.calendar.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.calendar.standard.FeedEventData;
import ru.android.healthvector.data.db.entities.calendar.standard.FeedEventEntity;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.dictionaries.FoodData;
import ru.android.healthvector.data.db.entities.dictionaries.FoodEntity;
import ru.android.healthvector.data.db.entities.dictionaries.FoodMeasureData;
import ru.android.healthvector.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.data.repositories.dictionaries.food.mappers.FoodMapper;
import ru.android.healthvector.data.repositories.dictionaries.foodmeasure.mappers.FoodMeasureMapper;
import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;

public class FeedEventMapper implements EntityMapper<FeedEventData, FeedEventEntity, FeedEvent> {
    private final ChildMapper childMapper;
    private final FoodMeasureMapper foodMeasureMapper;
    private final FoodMapper foodMapper;

    @Inject
    public FeedEventMapper(ChildMapper childMapper,
                           FoodMeasureMapper foodMeasureMapper,
                           FoodMapper foodMapper) {
        this.childMapper = childMapper;
        this.foodMeasureMapper = foodMeasureMapper;
        this.foodMapper = foodMapper;
    }

    @Override
    public FeedEvent mapToPlainObject(@NonNull FeedEventData feedEventData) {
        MasterEventData masterEventData = feedEventData.getMasterEvent();
        ChildData childData = masterEventData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        FoodMeasureData foodMeasureData = feedEventData.getFoodMeasure();
        FoodMeasure foodMeasure = foodMeasureData == null ? null : foodMeasureMapper.mapToPlainObject(foodMeasureData);
        FoodData foodData = feedEventData.getFood();
        Food food = foodData == null ? null : foodMapper.mapToPlainObject(foodData);
        return FeedEvent.builder()
                .id(feedEventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .dateTime(masterEventData.getDateTime())
                .notifyDateTime(masterEventData.getNotifyDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .child(child)
                .linearGroup(masterEventData.getLinearGroup())
                .feedType(feedEventData.getFeedType())
                .breast(feedEventData.getBreast())
                .leftDurationInMinutes(feedEventData.getLeftDurationInMinutes())
                .rightDurationInMinutes(feedEventData.getRightDurationInMinutes())
                .amount(feedEventData.getAmount())
                .amountMl(feedEventData.getAmountMl())
                .foodMeasure(foodMeasure)
                .food(food)
                .build();
    }

    @Override
    public FeedEventEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                       @NonNull FeedEvent feedEvent) {
        FeedEventEntity feedEventEntity;
        if (feedEvent.getId() == null) {
            feedEventEntity = new FeedEventEntity();
        } else {
            feedEventEntity = (FeedEventEntity) blockingEntityStore.findByKey(FeedEventEntity.class, feedEvent.getId());
        }
        fillNonReferencedFields(feedEventEntity, feedEvent);

        MasterEventEntity masterEventEntity = (MasterEventEntity) blockingEntityStore.findByKey(MasterEventEntity.class, feedEvent.getMasterEventId());
        feedEventEntity.setMasterEvent(masterEventEntity);

        FoodMeasure foodMeasure = feedEvent.getFoodMeasure();
        if (foodMeasure != null) {
            FoodMeasureEntity foodMeasureEntity = (FoodMeasureEntity) blockingEntityStore.findByKey(FoodMeasureEntity.class, foodMeasure.getId());
            feedEventEntity.setFoodMeasure(foodMeasureEntity);
        }
        Food food = feedEvent.getFood();
        if (food != null) {
            FoodEntity foodEntity = (FoodEntity) blockingEntityStore.findByKey(FoodEntity.class, food.getId());
            feedEventEntity.setFood(foodEntity);
        }
        return feedEventEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull FeedEventEntity to, @NonNull FeedEvent from) {
        to.setFeedType(from.getFeedType());
        to.setBreast(from.getBreast());
        to.setLeftDurationInMinutes(from.getLeftDurationInMinutes());
        to.setRightDurationInMinutes(from.getRightDurationInMinutes());
        to.setAmount(from.getAmount());
        to.setAmountMl(from.getAmountMl());
    }
}
