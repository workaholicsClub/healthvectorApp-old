package ru.android.childdiary.domain.interactors.medical.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

@Value
@Builder
public class DeleteMedicineTakingEventsRequest {
    @NonNull
    MedicineTaking medicineTaking;
    @Nullable
    Integer linearGroup;
}
