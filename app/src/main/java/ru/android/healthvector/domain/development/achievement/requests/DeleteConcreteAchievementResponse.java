package ru.android.healthvector.domain.development.achievement.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;

@Value
@Builder
public class DeleteConcreteAchievementResponse implements DeleteResponse {
    @NonNull
    DeleteConcreteAchievementRequest request;
    @NonNull
    List<String> imageFilesToDelete;
}
