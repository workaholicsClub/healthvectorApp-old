package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public abstract class EventsGenerator<From extends RepeatParametersContainer, To extends MasterEvent> {
    protected final ReactiveEntityStore<Persistable> dataStore;

    public EventsGenerator(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public void generateEvents(@NonNull From from) {
        DateTime fromDateTime = from.getDateTime();
        RepeatParameters repeatParameters = from.getRepeatParameters();
        int linearGroup = 0;
        for (int i = 0; i < 1000; ++i) {
            DateTime dateTime = fromDateTime.plusDays(i);
            To event = createEvent(from, dateTime, linearGroup);
            add(event);
        }
    }

    protected abstract To createEvent(From from, DateTime dateTime, int linearGroup);

    protected abstract To add(@NonNull To event);
}
