package ru.android.healthvector.data.repositories.core.generators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.healthvector.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.healthvector.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.medical.data.DoctorVisit;

public class DoctorVisitEventsGenerator extends EventsGenerator<DoctorVisit> {
    private final MasterEventMapper masterEventMapper;
    private final DoctorVisitEventMapper doctorVisitEventMapper;
    private List<MasterEventEntity> masterEvents;
    private List<DoctorVisitEventEntity> events;

    @Inject
    public DoctorVisitEventsGenerator(ReactiveEntityStore<Persistable> dataStore,
                                      MasterEventMapper masterEventMapper,
                                      DoctorVisitEventMapper doctorVisitEventMapper) {
        super(dataStore);
        this.masterEventMapper = masterEventMapper;
        this.doctorVisitEventMapper = doctorVisitEventMapper;
    }

    @Override
    protected void startInsertion() {
        masterEvents = new ArrayList<>();
        events = new ArrayList<>();
    }

    @Override
    protected void createEvent(@NonNull DoctorVisit doctorVisit,
                               @NonNull DateTime dateTime,
                               @Nullable Integer linearGroup) {
        DoctorVisitEvent event = DoctorVisitEvent.builder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.DOCTOR_VISIT)
                .dateTime(dateTime)
                .notifyDateTime(calculateNotifyTime(dateTime, doctorVisit.getNotifyTimeInMinutes()))
                .notifyTimeInMinutes(doctorVisit.getNotifyTimeInMinutes())
                .note(null)
                .isDone(null)
                .child(doctorVisit.getChild())
                .linearGroup(linearGroup)
                .doctorVisit(doctorVisit)
                .doctor(doctorVisit.getDoctor())
                .name(doctorVisit.getName())
                .durationInMinutes(doctorVisit.getDurationInMinutes())
                .imageFileName(null)
                .build();
        MasterEventEntity masterEventEntity = masterEventMapper.mapToEntity(blockingEntityStore, event);
        DoctorVisitEventEntity eventEntity = doctorVisitEventMapper.mapToEntity(blockingEntityStore, event);
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
