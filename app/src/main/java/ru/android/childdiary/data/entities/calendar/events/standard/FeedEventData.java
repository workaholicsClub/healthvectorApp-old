package ru.android.childdiary.data.entities.calendar.events.standard;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.MasterEvent;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.data.types.FoodMeasure;

@Table(name = "feed_event")
@Entity(name = "FeedEventEntity")
public interface FeedEventData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEvent getMasterEvent();

    FeedType getFeedType();

    Breast getBreast();

    int getDurationInMinutes();

    int getMilkAmountImMilliliters();

    int getFoodAmount();

    FoodMeasure getFoodMeasure();
}
