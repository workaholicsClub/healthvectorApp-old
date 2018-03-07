package ru.android.healthvector.domain.development.achievement.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;

@Value
@Builder(toBuilder = true)
public class UpsertConcreteAchievementRequest {
    @NonNull
    ConcreteAchievement concreteAchievement;
}
