package ru.android.healthvector.data.repositories.calendar;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.healthvector.data.repositories.core.ValueDataRepository;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.requests.GetEventsFilter;

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
