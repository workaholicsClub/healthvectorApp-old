package ru.android.childdiary.domain.development.achievement.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;

@Value
@Builder
public class DeleteConcreteAchievementRequest {
    @NonNull
    ConcreteAchievement concreteAchievement;
}
