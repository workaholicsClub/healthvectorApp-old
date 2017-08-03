package ru.android.childdiary.domain.interactors.child.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.data.Child;

@Value
@Builder
public class DeleteChildRequest {
    @NonNull
    Child child;
}
