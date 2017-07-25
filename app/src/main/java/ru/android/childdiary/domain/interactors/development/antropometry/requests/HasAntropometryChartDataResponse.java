package ru.android.childdiary.domain.interactors.development.antropometry.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class HasAntropometryChartDataResponse {
    @NonNull
    Child child;
    boolean hasData;
}