package ru.android.childdiary.presentation.calendar.partitions;

import org.joda.time.LocalDate;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.data.Child;

@Value
@Builder
class CalendarState {
    @NonNull
    Child child;
    @NonNull
    LocalDate date;
    @NonNull
    List<MasterEvent> events;
}
