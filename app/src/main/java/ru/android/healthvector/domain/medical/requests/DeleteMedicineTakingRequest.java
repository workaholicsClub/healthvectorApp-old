package ru.android.healthvector.domain.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.medical.data.MedicineTaking;

@Value
@Builder
public class DeleteMedicineTakingRequest {
    @NonNull
    MedicineTaking medicineTaking;
}
