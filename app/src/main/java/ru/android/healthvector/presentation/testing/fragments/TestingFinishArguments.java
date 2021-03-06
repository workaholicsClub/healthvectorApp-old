package ru.android.healthvector.presentation.testing.fragments;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.AppPartitionArguments;
import ru.android.healthvector.utils.strings.TestUtils;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TestingFinishArguments extends AppPartitionArguments {
    @NonNull
    Test test;
    @Nullable
    DomanTestParameter parameter;
    @NonNull
    String text;
    @NonNull
    List<DomanResult> results;
    boolean invalidResults;
    boolean isInTestMode;

    @Builder(builderMethodName = "testingFinishBuilder")
    public TestingFinishArguments(@NonNull Child child,
                                  @NonNull LocalDate selectedDate,
                                  @NonNull Test test,
                                  @Nullable DomanTestParameter parameter,
                                  @NonNull String text,
                                  @Nullable DomanResult result,
                                  boolean invalidResults,
                                  boolean isInTestMode) {
        super(child, selectedDate);
        this.test = test;
        this.parameter = parameter;
        this.text = text;
        this.results = result == null
                ? Collections.emptyList()
                : new ArrayList<>(TestUtils.filterResults(Collections.singletonList(result)));
        this.invalidResults = invalidResults;
        this.isInTestMode = isInTestMode;
    }
}
