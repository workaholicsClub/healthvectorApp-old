package ru.android.childdiary.data.db.entities.development;

import org.joda.time.LocalDate;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.types.AchievementType;

@Table(name = "concrete_achievement")
@Entity(name = "ConcreteAchievementEntity")
public interface ConcreteAchievementData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    AchievementType getAchievementType();

    String getNameEn();

    String getNameRu();

    String getNameUser();

    LocalDate getConcreteAchievementDate();

    String getNote();

    String getImageFileName();

    Boolean isPredefined();

    Double getFromAge();

    Double getToAge();
}
