package ru.android.childdiary.domain.interactors.development.testing.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.data.Child;

@Value
@Builder
public class TestResultsRequest {
    @NonNull
    Child child;
    @Nullable
    TestType testType;
    @Nullable
    DomanTestParameter testParameter;
    boolean ascending;
}
