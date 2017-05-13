package ru.android.childdiary.domain.interactors.calendar.requests;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

@Value
@Builder
public class DeleteEventsRequest {
    DeleteType deleteType;
    MasterEvent event;
    DoctorVisit doctorVisit;
    MedicineTaking medicineTaking;
    DateTime dateTime;
    boolean delete;

    public enum DeleteType {
        DELETE_ONE, DELETE_LINEAR_GROUP,
        DELETE_ALL_DOCTOR_VISIT_EVENTS, DELETE_ALL_MEDICINE_TAKING_EVENTS,
        COMPLETE_DOCTOR_VISIT, COMPLETE_MEDICINE_TAKING
    }
}
