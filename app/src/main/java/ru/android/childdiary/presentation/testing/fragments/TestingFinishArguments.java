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
import ru.android.childdiary.presentation.core.AppPartitionArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TestingFinishArguments extends AppPartitionArguments {
    @NonNull
    String text;
    @Nullable
    DomanTestParameter parameter;

    @Builder(builderMethodName = "testingFinishBuilder")
    public TestingFinishArguments(@NonNull Child child,
                                  @NonNull LocalDate selectedDate,
                                  @NonNull String text,
                                  @Nullable DomanTestParameter parameter) {
        super(child, selectedDate);
        this.text = text;
        this.parameter = parameter;
    }
}
