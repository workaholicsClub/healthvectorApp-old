package ru.android.childdiary.data.db.entities.development.achievement;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

// TODO: translation table
@Table(name = "achievement")
@Entity(name = "AchievementEntity")
public interface AchievementData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getName();
}
