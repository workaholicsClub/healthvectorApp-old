package ru.android.childdiary.domain.core.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@Builder
public class HasDataResponse {
    @NonNull
    Child child;
    boolean hasData;
}
