package ru.android.childdiary.domain.interactors.calendar.requests;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class AddSleepRequest {
    Child child;
    SleepEvent event;
}
