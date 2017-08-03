package ru.android.childdiary.domain.interactors.development.antropometry.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.data.Child;

@Value
@Builder
public class AntropometryListRequest {
    @NonNull
    Child child;
    boolean ascending;
    boolean startFromBirthday;
}
