package ru.android.childdiary.domain.interactors.calendar.requests;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class AddEventRequest<T extends MasterEvent> {
    Child child;
    T event;
}
