package ru.android.childdiary.domain.interactors.development.achievement.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;

@Value
@Builder
public class DeleteConcreteAchievementRequest {
    @NonNull
    ConcreteAchievement concreteAchievement;
}
