package ru.android.healthvector.domain.development.antropometry.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.antropometry.data.AntropometryPoint;

@Value
@Builder
public class WhoNormRequest {
    @NonNull
    Child child;
    @NonNull
    List<AntropometryPoint> points;
}
