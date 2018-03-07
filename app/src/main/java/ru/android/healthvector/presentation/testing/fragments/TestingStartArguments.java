package ru.android.healthvector.presentation.testing.fragments;

import org.joda.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.AppPartitionArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TestingStartArguments extends AppPartitionArguments {
    @NonNull
    Test test;

    @Builder(builderMethodName = "testingStartBuilder")
    public TestingStartArguments(@NonNull Child child,
                                 @NonNull LocalDate selectedDate,
                                 @NonNull Test test) {
        super(child, selectedDate);
        this.test = test;
    }
}
