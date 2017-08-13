package ru.android.childdiary.domain.medical.requests;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.medical.data.MedicineTaking;

@Value
@Builder
public class CompleteMedicineTakingRequest {
    @NonNull
    MedicineTaking medicineTaking;
    @NonNull
    DateTime dateTime;
    boolean delete;
}
