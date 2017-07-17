package ru.android.childdiary.domain.interactors.development.testing;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class TestResult implements Serializable {
    Long id;

    Child child;

    TestType testType;

    LocalDate birthDate;

    LocalDate date;

    DomanTestParameter domanTestParameter;

    // Доман -- Стадия развития
    // Аутизм -- Количество баллов (риск)
    // Новрожденный -- Есть риск (1), Норма (0)
    Integer result;
}
