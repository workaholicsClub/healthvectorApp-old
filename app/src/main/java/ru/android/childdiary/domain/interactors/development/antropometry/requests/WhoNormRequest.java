package ru.android.childdiary.domain.interactors.development.antropometry.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.data.AntropometryPoint;

@Value
@Builder
public class WhoNormRequest {
    @NonNull
    Child child;
    @NonNull
    List<AntropometryPoint> points;
}
