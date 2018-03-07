package ru.android.healthvector.data.repositories.core.generators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.healthvector.data.repositories.calendar.mappers.MedicineTakingEventMapper;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.medical.data.MedicineTaking;

public class MedicineTakingEventsGenerator extends EventsGenerator<MedicineTaking> {
    private final MasterEventMapper masterEventMapper;
    private final MedicineTakingEventMapper medicineTakingEventMapper;
    private List<MasterEventEntity> masterEvents;
    private List<MedicineTakingEventEntity> events;

    @Inject
    public MedicineTakingEventsGenerator(ReactiveEntityStore<Persistable> dataStore,
                                         MasterEventMapper masterEventMapper,
                                         MedicineTakingEventMapper medicineTakingEventMapper) {
        super(dataStore);
        this.masterEventMapper = masterEventMapper;
        this.medicineTakingEventMapper = medicineTakingEventMapper;
    }

    @Override
    protected void startInsertion() {
        masterEvents = new ArrayList<>();
        events = new ArrayList<>();
    }

    @Override
    protected void createEvent(@NonNull MedicineTaking medicineTaking,
                               @NonNull DateTime dateTime,
                               @Nullable Integer linearGroup) {
        MedicineTakingEvent event = MedicineTakingEvent.builder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.MEDICINE_TAKING)
                .dateTime(dateTime)
                .notifyDateTime(calculateNotifyTime(dateTime, medicineTaking.getNotifyTimeInMinutes()))
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
        MasterEventEntity masterEventEntity = masterEventMapper.mapToEntity(blockingEntityStore, event);
        MedicineTakingEventEntity eventEntity = medicineTakingEventMapper.mapToEntity(blockingEntityStore, event);
        eventEntity.setMasterEvent(masterEventEntity);
        masterEvents.add(masterEventEntity);
        events.add(eventEntity);
    }

    @Override
    protected void finishInsertion() {
        blockingEntityStore.insert(masterEvents);
        blockingEntityStore.insert(events);
    }
}
