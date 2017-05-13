package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.childdiary.data.repositories.calendar.mappers.MasterEventMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

public class DoctorVisitEventsGenerator extends EventsGenerator<DoctorVisit, DoctorVisitEvent> {
    private final MasterEventMapper masterEventMapper;
    private final DoctorVisitEventMapper doctorVisitEventMapper;

    @Inject
    public DoctorVisitEventsGenerator(ReactiveEntityStore<Persistable> dataStore,
                                      MasterEventMapper masterEventMapper,
                                      DoctorVisitEventMapper doctorVisitEventMapper) {
        super(dataStore);
        this.masterEventMapper = masterEventMapper;
        this.doctorVisitEventMapper = doctorVisitEventMapper;
    }

    @Override
    protected DoctorVisitEvent createEvent(@NonNull DoctorVisit doctorVisit,
                                           @NonNull DateTime dateTime,
                                           @Nullable Integer linearGroup) {
        return DoctorVisitEvent.builder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.DOCTOR_VISIT)
                .dateTime(dateTime)
                .notifyTimeInMinutes(doctorVisit.getNotifyTimeInMinutes())
                .note(null)
                .isDone(null)
                .child(doctorVisit.getChild())
                .repeatParameters(doctorVisit.getRepeatParameters())
                .linearGroup(linearGroup)
                .doctorVisit(doctorVisit)
                .doctor(doctorVisit.getDoctor())
                .name(doctorVisit.getName())
                .durationInMinutes(doctorVisit.getDurationInMinutes())
                .imageFileName(null)
                .build();
    }

    @Override
    protected DoctorVisitEvent add(@NonNull DoctorVisitEvent event) {
        MasterEvent masterEvent = DbUtils.insert(dataStore, event, masterEventMapper);
        DoctorVisitEvent doctorVisitEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
        return DbUtils.insert(dataStore, doctorVisitEvent, doctorVisitEventMapper);
    }
}
