package ru.android.childdiary.domain.interactors.development.achievement.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.DeleteResponse;

@Value
@Builder
public class DeleteConcreteAchievementResponse implements DeleteResponse {
    @NonNull
    DeleteConcreteAchievementRequest request;
    @NonNull
    List<String> imageFilesToDelete;
}
