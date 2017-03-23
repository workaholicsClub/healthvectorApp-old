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
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@EqualsAndHashCode(callSuper = true)
public class FeedEvent extends MasterEvent {
    Long id;

    FeedType feedType;

    Breast breast;

    Integer leftDurationInMinutes;

    Integer rightDurationInMinutes;

    Double amount;

    Double amountMl;

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
                      Child child,
                      Long id,
                      FeedType feedType,
                      Breast breast,
                      Integer leftDurationInMinutes,
                      Integer rightDurationInMinutes,
                      Double amount,
                      Double amountMl,
                      FoodMeasure foodMeasure) {
        super(masterEventId, eventType, description, dateTime, notifyTimeInMinutes, note, isDone, isDeleted, child);
        this.id = id;
        this.feedType = feedType;
        this.breast = breast;
        this.leftDurationInMinutes = leftDurationInMinutes;
        this.rightDurationInMinutes = rightDurationInMinutes;
        this.amount = amount;
        this.amountMl = amountMl;
        this.foodMeasure = foodMeasure;
    }
}
