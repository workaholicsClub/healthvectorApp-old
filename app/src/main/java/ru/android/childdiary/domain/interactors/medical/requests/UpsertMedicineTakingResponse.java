package ru.android.childdiary.domain.interactors.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

@Value
@Builder
public class UpsertMedicineTakingResponse {
    @NonNull
    UpsertMedicineTakingRequest request;
    int addedEventsCount;
    @NonNull
    MedicineTaking medicineTaking;
}
