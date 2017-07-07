package ru.android.childdiary.domain.interactors.development.testing;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder(toBuilder = true)
public class TestResult implements Serializable {
    Long id;

    Child child;

    DateTime dateTime;

    TestType testType;

    DomanTestParameter testParameter;

    Integer result;
}
