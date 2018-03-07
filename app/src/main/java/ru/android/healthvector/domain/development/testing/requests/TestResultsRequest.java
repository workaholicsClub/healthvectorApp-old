package ru.android.healthvector.domain.development.testing.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.domain.child.data.Child;

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
