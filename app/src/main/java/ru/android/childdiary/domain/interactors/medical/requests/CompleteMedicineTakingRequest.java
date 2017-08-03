package ru.android.childdiary.domain.interactors.medical.requests;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;

@Value
@Builder
public class CompleteMedicineTakingRequest {
    @NonNull
    MedicineTaking medicineTaking;
    @NonNull
    DateTime dateTime;
    boolean delete;
}
