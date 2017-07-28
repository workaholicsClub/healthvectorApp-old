package ru.android.childdiary.data.db.entities.development.antropometry;

import org.joda.time.LocalDate;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.data.db.entities.child.ChildData;

@Table(name = "antropometry")
@Entity(name = "AntropometryEntity")
public interface AntropometryData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    Double getHeight();

    Double getWeight();

    LocalDate getAntropometryDate();
}
