package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.repositories.calendar.mappers.DoctorVisitEventMapper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

public class DoctorVisitEventsGenerator extends EventsGenerator<DoctorVisit, DoctorVisitEvent> {
    @Inject
    public DoctorVisitEventsGenerator(ReactiveEntityStore<Persistable> dataStore) {
        super(dataStore);
    }

    @Override
    protected DoctorVisitEvent createEvent(DoctorVisit doctorVisit, DateTime dateTime, int linearGroup) {
        return DoctorVisitEvent.builder()
                .id(null)
                .masterEventId(null)
                .eventType(EventType.DOCTOR_VISIT)
                .dateTime(dateTime)
                .notifyTimeInMinutes(doctorVisit.getNotifyTimeInMinutes())
                .note(doctorVisit.getNote())
                .isDone(null)
                .child(doctorVisit.getChild())
                .repeatParameters(doctorVisit.getRepeatParameters())
                .linearGroup(linearGroup)
                .doctorVisit(doctorVisit)
                .build();
    }

    // TODO: mapper, datastore private
    @Override
    protected DoctorVisitEvent insert(@NonNull DoctorVisitEvent event, @NonNull MasterEvent masterEvent) {
        DoctorVisitEvent doctorVisitEvent = event.toBuilder().masterEventId(masterEvent.getMasterEventId()).build();
        return DbUtils.insert(dataStore, event,
                DoctorVisitEventMapper::mapToEntity, DoctorVisitEventMapper::mapToPlainObject);
    }
}
