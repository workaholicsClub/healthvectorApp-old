package ru.android.childdiary.domain.interactors.development.testing;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class TestResult implements Serializable {
    Long id;

    Child child;

    TestType testType;

    Test test;

    LocalDate date;

    // Доман -- Стадия развития
    // Аутизм -- Количество баллов (риск)
    // Новрожденный -- Есть риск (1), Норма (0)
    Integer result;

    LocalDate birthDate;

    LocalDate domanDate;

    DomanTestParameter domanTestParameter;

    public boolean isInvalid() {
        return !ObjectUtils.equals(child.getBirthDate(), birthDate);
    }
}
