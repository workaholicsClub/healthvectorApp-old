package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.db.entities.calendar.events.core.MasterEventEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

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
