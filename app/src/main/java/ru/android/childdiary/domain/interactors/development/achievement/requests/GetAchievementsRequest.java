package ru.android.childdiary.domain.interactors.development.achievement.requests;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAchievementsRequest {
    boolean isPredefined;
}
