package ru.android.childdiary.presentation.testing.fragments;

import org.joda.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TestingQuestionArguments extends TestingStartArguments {
    @Builder(builderMethodName = "testingQuestionBuilder")
    public TestingQuestionArguments(@NonNull Child child,
                                    @NonNull LocalDate selectedDate,
                                    @NonNull Test test) {
        super(child, selectedDate, test);
    }
}
