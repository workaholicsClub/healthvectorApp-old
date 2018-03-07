package ru.android.healthvector.domain.child.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;

@Value
@Builder
public class DeleteChildRequest {
    @NonNull
    Child child;
}
