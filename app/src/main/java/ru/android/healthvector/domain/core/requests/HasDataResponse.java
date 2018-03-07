package ru.android.healthvector.domain.core.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;

@Value
@Builder
public class HasDataResponse {
    @NonNull
    Child child;
    boolean hasData;
}
