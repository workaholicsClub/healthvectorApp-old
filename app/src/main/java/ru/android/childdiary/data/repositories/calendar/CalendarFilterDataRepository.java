package ru.android.childdiary.data.repositories.calendar;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.core.ValueDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.requests.GetEventsFilter;

@Singleton
public class CalendarFilterDataRepository extends ValueDataRepository<GetEventsFilter> {
    @Inject
    public CalendarFilterDataRepository() {
    }

    @Override
    protected GetEventsFilter getDefaultValue() {
        return GetEventsFilter.builder()
                .eventTypes(Arrays.asList(EventType.values()))
                .build();
    }
}
