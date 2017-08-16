package ru.android.childdiary.domain.child.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.requests.DeleteResponse;

@Value
@Builder
public class DeleteChildResponse implements DeleteResponse {
    @NonNull
    DeleteChildRequest request;
    @NonNull
    List<String> imageFilesToDelete;
}