package ru.android.childdiary.domain.interactors.calendar.requests;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class AddOtherRequest {
    Child child;
    OtherEvent event;
}
