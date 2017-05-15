package ru.android.childdiary.domain.interactors.medical.requests;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Value
@Builder(toBuilder = true)
public class MedicineTakingListRequest {
    Child child;
    DateTime fromDateTime;
    DateTime toDateTime;
    Medicine medicine;
}
