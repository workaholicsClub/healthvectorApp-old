package ru.android.healthvector.domain.development.antropometry.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;

@Value
@Builder
public class AntropometryListRequest {
    @NonNull
    Child child;
    boolean ascending;
    boolean startFromBirthday;
}
