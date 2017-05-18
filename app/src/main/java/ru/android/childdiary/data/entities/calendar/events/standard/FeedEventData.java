package ru.android.childdiary.data.entities.calendar.events.standard;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.FoodData;
import ru.android.childdiary.data.entities.calendar.events.core.FoodMeasureData;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.FeedType;

@Table(name = "feed_event")
@Entity(name = "FeedEventEntity")
public interface FeedEventData extends Persistable {
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
    FoodMeasureData getFoodMeasure();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    FoodData getFood();
}
