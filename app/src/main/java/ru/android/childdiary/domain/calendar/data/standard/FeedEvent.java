package ru.android.childdiary.domain.calendar.data.standard;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupItem;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.domain.dictionaries.food.data.Food;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeedEvent extends MasterEvent implements ContentObject<FeedEvent>, LinearGroupItem<FeedEvent> {
    private static final FeedEvent NULL = FeedEvent.builder().build();

    Long id;

    FeedType feedType;

    Breast breast;

    Integer leftDurationInMinutes;

    Integer rightDurationInMinutes;

    Double amount;

    Double amountMl;

    FoodMeasure foodMeasure;

    Food food;

    @Builder(toBuilder = true)
    private FeedEvent(Long masterEventId,
                      EventType eventType,
                      DateTime dateTime,
                      DateTime notifyDateTime,
                      Integer notifyTimeInMinutes,
                      String note,
                      Boolean isDone,
                      Child child,
                      Integer linearGroup,
                      Long id,
                      FeedType feedType,
                      Breast breast,
                      Integer leftDurationInMinutes,
                      Integer rightDurationInMinutes,
                      Double amount,
                      Double amountMl,
                      FoodMeasure foodMeasure,
                      Food food) {
        super(masterEventId, eventType, dateTime, notifyDateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.feedType = feedType;
        this.breast = breast;
        this.leftDurationInMinutes = leftDurationInMinutes;
        this.rightDurationInMinutes = rightDurationInMinutes;
        this.amount = amount;
        this.amountMl = amountMl;
        this.foodMeasure = foodMeasure;
        this.food = food;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull FeedEvent other) {
        if (!contentEquals(this, other)) {
            return false;
        }
        if (getFeedType() != other.getFeedType()) {
            return false;
        }
        if (getFeedType() == null) {
            return true;
        }
        switch (getFeedType()) {
            case BREAST_MILK:
                return getBreast() == other.getBreast()
                        && ObjectUtils.equals(getLeftDurationInMinutes(), other.getLeftDurationInMinutes())
                        && ObjectUtils.equals(getRightDurationInMinutes(), other.getRightDurationInMinutes());
            case PUMPED_MILK:
                return ObjectUtils.equals(getAmountMl(), other.getAmountMl());
            case MILK_FORMULA:
                return ObjectUtils.equals(getAmountMl(), other.getAmountMl());
            case FOOD:
                return ObjectUtils.equals(getAmount(), other.getAmount())
                        && ObjectUtils.contentEquals(getFood(), other.getFood())
                        && ObjectUtils.contentEquals(getFoodMeasure(), other.getFoodMeasure());
        }
        return false;
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(FeedEvent other) {
        return Collections.emptyList();
    }
}
