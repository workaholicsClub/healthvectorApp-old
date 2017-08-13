package ru.android.childdiary.domain.child.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@Builder
public class DeleteChildRequest {
    @NonNull
    Child child;
}
