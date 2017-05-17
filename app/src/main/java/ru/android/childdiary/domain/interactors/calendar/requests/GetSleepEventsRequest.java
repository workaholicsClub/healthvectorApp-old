package ru.android.childdiary.domain.interactors.calendar.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class GetSleepEventsRequest {
    @Nullable
    Child child;
    boolean withStartedTimer;
}
