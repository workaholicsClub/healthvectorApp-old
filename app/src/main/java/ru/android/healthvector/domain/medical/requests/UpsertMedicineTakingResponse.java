package ru.android.healthvector.domain.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.medical.data.MedicineTaking;

@Value
@Builder
public class UpsertMedicineTakingResponse implements DeleteResponse {
    @NonNull
    UpsertMedicineTakingRequest request;
    int addedEventsCount;
    @NonNull
    MedicineTaking medicineTaking;
    @NonNull
    List<String> imageFilesToDelete;
}
