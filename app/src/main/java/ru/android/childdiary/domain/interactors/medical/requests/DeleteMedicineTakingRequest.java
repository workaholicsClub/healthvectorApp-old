package ru.android.childdiary.domain.interactors.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;

@Value
@Builder
public class DeleteMedicineTakingRequest {
    @NonNull
    MedicineTaking medicineTaking;
}
