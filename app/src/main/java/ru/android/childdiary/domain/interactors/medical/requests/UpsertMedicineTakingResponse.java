package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

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