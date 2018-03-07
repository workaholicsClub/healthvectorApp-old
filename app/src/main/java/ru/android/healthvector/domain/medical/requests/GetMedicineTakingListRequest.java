package ru.android.healthvector.domain.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;

@Value
@Builder
public class GetMedicineTakingListRequest {
    @NonNull
    Child child;
    @NonNull
    GetMedicineTakingListFilter filter;
}
