package ru.android.childdiary.domain.interactors.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

@Value
@Builder(toBuilder = true)
public class UpsertMedicineTakingRequest {
    @NonNull
    MedicineTaking medicineTaking;
}
