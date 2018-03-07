package ru.android.healthvector.domain.development.achievement.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;

@Value
@Builder
public class UpsertConcreteAchievementResponse implements DeleteResponse {
    @NonNull
    UpsertConcreteAchievementRequest request;
    @NonNull
    ConcreteAchievement concreteAchievement;
    @NonNull
    List<String> imageFilesToDelete;
}
