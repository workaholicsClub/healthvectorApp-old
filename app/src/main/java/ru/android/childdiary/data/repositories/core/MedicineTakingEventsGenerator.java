package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MedicineTakingEventMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

public class MedicineTakingEventsGenerator extends EventsGenerator<MedicineTaking, MedicineTakingEvent> {
    private final MasterEventMapper masterEventMapper;
    private final MedicineTakingEventMapper medicineTakingEventMapper;

    @Inject
    public MedicineTakingEventsGenerator(ReactiveEntityStore<Persistable> dataStore,
                                         MasterEventMapper masterEventMapper,
                                         MedicineTakingEventMapper medicineTakingEventMapper) {
        super(dataStore);
        this.masterEventMapper = masterEventMapper;
        this.medicineTakingEventMapper = medicineTakingEventMapper;
    }

    @Override
    protected MedicineTakingEvent createEvent(@NonNull MedicineTaking medicineTaking,
                                              @NonNull DateTime dateTime,
                                              @Nullable Integer linearGroup) {
        return MedicineTakingEvent.builder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.MEDICINE_TAKING)
                .dateTime(dateTime)
                .notifyTimeInMinutes(medicineTaking.getNotifyTimeInMinutes())
                .note(null)
                .isDone(null)
                .child(medicineTaking.getChild())
                .linearGroup(linearGroup)
                .medicineTaking(medicineTaking)
                .medicine(medicineTaking.getMedicine())
                .amount(medicineTaking.getAmount())
                .medicineMeasure(medicineTaking.getMedicineMeasure())
                .imageFileName(null)
                .build();
    }

    @Override
    protected MedicineTakingEvent add(@NonNull MedicineTakingEvent event) {
        MasterEvent masterEvent = DbUtils.insert(blockingEntityStore, event, masterEventMapper);
        MedicineTakingEvent medicineTakingEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
        return DbUtils.insert(blockingEntityStore, medicineTakingEvent, medicineTakingEventMapper);
    }
}
