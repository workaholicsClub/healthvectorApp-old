package ru.android.childdiary.presentation.testing.fragments;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionArguments;

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
    @Nullable
    Boolean forward;

    @Builder(builderMethodName = "testingQuestionBuilder")
    public TestingQuestionArguments(@NonNull Child child,
                                    @NonNull LocalDate selectedDate,
                                    @NonNull Test test,
                                    @Nullable DomanTestParameter parameter,
                                    @NonNull Question question,
                                    @Nullable Boolean forward) {
        super(child, selectedDate);
        this.test = test;
        this.parameter = parameter;
        this.question = question;
        this.forward = forward;
    }
}
