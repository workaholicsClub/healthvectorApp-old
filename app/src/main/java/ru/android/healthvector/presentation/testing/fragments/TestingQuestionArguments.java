package ru.android.healthvector.presentation.testing.fragments;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.tests.core.Question;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.AppPartitionArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TestingQuestionArguments extends AppPartitionArguments {
    @NonNull
    Test test;
    @Nullable
    DomanTestParameter parameter;
    @NonNull
    Question question;

    @Builder(builderMethodName = "testingQuestionBuilder")
    public TestingQuestionArguments(@NonNull Child child,
                                    @NonNull LocalDate selectedDate,
                                    @NonNull Test test,
                                    @Nullable DomanTestParameter parameter,
                                    @NonNull Question question) {
        super(child, selectedDate);
        this.test = test;
        this.parameter = parameter;
        this.question = question;
    }
}
