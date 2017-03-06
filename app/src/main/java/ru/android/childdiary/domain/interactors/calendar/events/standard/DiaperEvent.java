package ru.android.childdiary.domain.interactors.calendar.events.standard;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

@Value
@Builder
public class DiaperEvent {
    Long id;

    MasterEvent masterEvent;

    DiaperState diaperState;
}
