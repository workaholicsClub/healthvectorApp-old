package ru.android.childdiary.domain.calendar.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@Builder
public class GetDoctorVisitEventsRequest {
    @Nullable
    Child child;
    boolean withImages;
}
