package ru.android.healthvector.data.db.entities.dictionaries;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

@Table(name = "achievement")
@Entity(name = "AchievementEntity")
public interface AchievementData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getNameEn();

    String getNameRu();

    String getNameUser();
}
