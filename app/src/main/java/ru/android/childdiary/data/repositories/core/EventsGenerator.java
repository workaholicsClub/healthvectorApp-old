package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public abstract class EventsGenerator<From extends RepeatParametersContainer, To extends MasterEvent> {
    protected final ReactiveEntityStore<Persistable> dataStore;

    public EventsGenerator(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public void generateEvents(@NonNull From from) {
        DateTime dateTime = from.getDateTime();
        RepeatParameters repeatParameters = from.getRepeatParameters();
        int linearGroup = 0;
        To event = createEvent(from, dateTime, linearGroup);
        add(event);
    }

    protected abstract To createEvent(From from, DateTime dateTime, int linearGroup);

    private To add(@NonNull To event) {
        MasterEvent masterEvent = insert(event);
        return insert(event, masterEvent);
    }

    private MasterEvent insert(@NonNull MasterEvent event) {
        return DbUtils.insert(dataStore, event,
                MasterEventMapper::mapToEntity, MasterEventMapper::mapToPlainObject);
    }

    protected abstract To insert(@NonNull To event, @NonNull MasterEvent masterEvent);
}
