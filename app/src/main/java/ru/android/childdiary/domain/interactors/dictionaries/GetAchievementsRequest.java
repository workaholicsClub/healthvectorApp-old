package ru.android.childdiary.domain.interactors.dictionaries;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAchievementsRequest {
    boolean isPredefined;
}
