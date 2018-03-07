package ru.android.healthvector.domain.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;

@Value
@Builder
public class UpdateMedicineTakingEventResponse implements DeleteResponse {
    @NonNull
    UpdateMedicineTakingEventRequest request;
    @NonNull
    MedicineTakingEvent medicineTakingEvent;
    @NonNull
    List<String> imageFilesToDelete;
}
