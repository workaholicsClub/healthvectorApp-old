package ru.android.healthvector.data.db.entities.development;

import org.joda.time.LocalDate;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;

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

    LocalDate getTestResultDate();

    // Доман -- Стадия развития
    // Аутизм -- Количество баллов (риск)
    // Новорожденный -- Есть риск (1), Норма (0)
    Integer getResultNumber();

    LocalDate getBirthDate();

    LocalDate getDomanDate();

    DomanTestParameter getDomanTestParameter();
}
