package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.DeleteResponse;

@Value
@Builder
public class DeleteMedicineTakingEventsResponse implements DeleteResponse {
    @NonNull
    DeleteMedicineTakingEventsRequest request;
    int count;
    @NonNull
    List<String> imageFilesToDelete;
}
