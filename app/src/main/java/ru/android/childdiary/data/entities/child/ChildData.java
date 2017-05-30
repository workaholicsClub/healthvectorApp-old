package ru.android.childdiary.data.entities.child;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.development.achievement.ConcreteAchievementData;
import ru.android.childdiary.data.entities.development.antropometry.AntropometryData;
import ru.android.childdiary.data.types.Sex;

@Table(name = "child")
@Entity(name = "ChildEntity")
public interface ChildData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getName();

    LocalDate getBirthDate();

    // необязательный параметр
    LocalTime getBirthTime();

    Sex getSex();

    // необязательный параметр
    String getImageFileName();

    Double getBirthHeight();

    Double getBirthWeight();

    @OneToMany
    List<MasterEventData> getEvents();

    @OneToMany
    List<AntropometryData> getAntropometryList();

    @OneToMany
    List<ConcreteAchievementData> getConcreteAchievements();
}
