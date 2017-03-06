package ru.android.childdiary.domain.interactors.calendar.events.standard;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.data.types.FoodMeasure;

@Value
@Builder
public class FeedEvent {
    Long id;

    MasterEventData masterEvent;

    FeedType feedType;

    Breast breast;

    Integer durationInMinutes;

    Integer milkAmountImMilliliters;

    Integer foodAmount;

    FoodMeasure foodMeasure;
}
