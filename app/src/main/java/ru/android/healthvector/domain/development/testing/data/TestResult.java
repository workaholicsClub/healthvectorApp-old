package ru.android.healthvector.domain.development.testing.data;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.utils.ObjectUtils;

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
    // Новорожденный -- Есть риск (1), Норма (0)
    Integer result;

    LocalDate birthDate;

    LocalDate domanDate;

    DomanTestParameter domanTestParameter;

    public boolean isInvalid() {
        return !ObjectUtils.equals(child.getBirthDate(), birthDate);
    }
}
