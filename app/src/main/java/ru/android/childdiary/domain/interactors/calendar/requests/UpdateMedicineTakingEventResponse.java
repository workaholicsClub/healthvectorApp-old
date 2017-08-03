package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.requests.DeleteResponse;
import ru.android.childdiary.domain.interactors.calendar.data.MedicineTakingEvent;

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
