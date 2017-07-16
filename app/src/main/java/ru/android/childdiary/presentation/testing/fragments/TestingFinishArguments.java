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
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.presentation.core.AppPartitionArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TestingFinishArguments extends AppPartitionArguments {
    @Nullable
    DomanTestParameter parameter;
    @NonNull
    String text;
    @Nullable
    DomanResult result;

    @Builder(builderMethodName = "testingFinishBuilder")
    public TestingFinishArguments(@NonNull Child child,
                                  @NonNull LocalDate selectedDate,
                                  @Nullable DomanTestParameter parameter,
                                  @NonNull String text,
                                  @Nullable DomanResult result) {
        super(child, selectedDate);
        this.parameter = parameter;
        this.text = text;
        this.result = result;
    }
}
