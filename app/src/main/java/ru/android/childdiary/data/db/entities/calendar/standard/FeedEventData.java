package ru.android.childdiary.data.db.entities.calendar.standard;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.db.entities.dictionaries.FoodData;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureData;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventData;
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

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    FoodMeasureData getFoodMeasure();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    FoodData getFood();
}
