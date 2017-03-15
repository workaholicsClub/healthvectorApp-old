package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class FeedEvent extends MasterEvent {
    Long id;

    FeedType feedType;

    Breast breast;

    Integer durationInMinutes;

    Integer milkAmountImMilliliters;

    Integer foodAmount;

    FoodMeasure foodMeasure;

    @Builder(toBuilder = true)
    private FeedEvent(Long masterEventId,
                      EventType eventType,
                      String description,
                      DateTime dateTime,
                      Integer notifyTimeInMinutes,
                      String note,
                      Boolean isDone,
                      Boolean isDeleted,
                      Long id,
                      FeedType feedType,
                      Breast breast,
                      Integer durationInMinutes,
                      Integer milkAmountImMilliliters,
                      Integer foodAmount,
                      FoodMeasure foodMeasure) {
        super(masterEventId, eventType, description, dateTime, notifyTimeInMinutes, note, isDone, isDeleted);
        this.id = id;
        this.feedType = feedType;
        this.breast = breast;
        this.durationInMinutes = durationInMinutes;
        this.milkAmountImMilliliters = milkAmountImMilliliters;
        this.foodAmount = foodAmount;
        this.foodMeasure = foodMeasure;
    }
}
