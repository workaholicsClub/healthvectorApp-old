package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;

import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventData;
import ru.android.childdiary.data.entities.calendar.events.standard.FeedEventEntity;
import ru.android.childdiary.data.entities.calendar.events.standard.FoodMeasureData;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;

class FeedEventMapper {
    public static FeedEvent mapToPlainObject(@NonNull FeedEventData eventData) {
        MasterEventData masterEventData = eventData.getMasterEvent();
        FoodMeasureData foodMeasureData = eventData.getFoodMeasureData();
        FoodMeasure foodMeasure = foodMeasureData == null ? null : FoodMeasureMapper.mapToPlainObject(foodMeasureData);
        return FeedEvent.builder()
                .id(eventData.getId())
                .masterEventId(masterEventData.getId())
                .eventType(masterEventData.getEventType())
                .description(masterEventData.getDescription())
                .dateTime(masterEventData.getDateTime())
                .notifyTimeInMinutes(masterEventData.getNotifyTimeInMinutes())
                .note(masterEventData.getNote())
                .isDone(masterEventData.isDone())
                .isDeleted(masterEventData.isDeleted())
                .feedType(eventData.getFeedType())
                .breast(eventData.getBreast())
                .durationInMinutes(eventData.getDurationInMinutes())
                .milkAmountImMilliliters(eventData.getMilkAmountImMilliliters())
                .foodAmount(eventData.getFoodAmount())
                .foodMeasure(foodMeasure)
                .build();
    }

    public static FeedEventEntity mapToEntity(@NonNull FeedEvent event) {
        return updateEntityWithPlainObject(new FeedEventEntity(), event);
    }

    public static FeedEventEntity mapToEntity(@NonNull FeedEvent event, @NonNull MasterEventEntity masterEventEntity) {
        FeedEventEntity eventEntity = updateEntityWithPlainObject(new FeedEventEntity(), event);
        eventEntity.setMasterEvent(masterEventEntity);
        return eventEntity;
    }

    public static FeedEventEntity updateEntityWithPlainObject(@NonNull FeedEventEntity to, @NonNull FeedEvent from) {
        to.setFeedType(from.getFeedType());
        to.setBreast(from.getBreast());
        to.setDurationInMinutes(from.getDurationInMinutes());
        to.setMilkAmountImMilliliters(from.getMilkAmountImMilliliters());
        to.setFoodAmount(from.getFoodAmount());
        // TODO: food measure update
        return to;
    }
}
