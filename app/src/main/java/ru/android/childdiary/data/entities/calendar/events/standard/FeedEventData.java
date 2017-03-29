package ru.android.childdiary.data.entities.calendar.events.standard;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.FoodData;
import ru.android.childdiary.data.entities.calendar.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.FeedType;

@Table(name = "feed_event")
@Entity(name = "FeedEventEntity")
public interface FeedEventData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    FeedType getFeedType();

    Breast getBreast();

    Integer getLeftDurationInMinutes();

    Integer getRightDurationInMinutes();

    Double getAmount();

    Double getAmountMl();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    FoodMeasureData getFoodMeasureData();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    FoodData getFoodData();
}
