package ru.android.childdiary.data.entities.development.achievement;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.child.ChildData;

@Table(name = "concrete_achievement")
@Entity(name = "ConcreteAchievementEntity")
public interface ConcreteAchievementData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    AchievementData getAchievement();

    String getName();

    DateTime getDateTime();

    String getNote();

    String getImageFileName();
}
