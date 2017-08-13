package ru.android.childdiary.domain.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.medical.data.MedicineTaking;

@Value
@Builder(toBuilder = true)
public class UpsertMedicineTakingRequest {
    @NonNull
    MedicineTaking medicineTaking;
}
