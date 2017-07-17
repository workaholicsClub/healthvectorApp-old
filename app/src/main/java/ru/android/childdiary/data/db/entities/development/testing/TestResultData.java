package ru.android.childdiary.data.db.entities.development.testing;

import org.joda.time.LocalDate;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.data.db.entities.child.ChildData;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;

@Table(name = "test_result")
@Entity(name = "TestResultEntity")
public interface TestResultData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    TestType getTestType();

    LocalDate getBirthDate();

    LocalDate getDate();

    DomanTestParameter getDomanTestParameter();

    // Доман -- Стадия развития
    // Аутизм -- Количество баллов (риск)
    // Новрожденный -- Есть риск (1), Норма (0)
    Integer getResult();
}
