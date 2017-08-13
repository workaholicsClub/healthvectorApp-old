package ru.android.childdiary.domain.development.antropometry.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@Builder
public class AntropometryListRequest {
    @NonNull
    Child child;
    boolean ascending;
    boolean startFromBirthday;
}
