package ru.android.childdiary.domain.interactors.development.achievement.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;

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
