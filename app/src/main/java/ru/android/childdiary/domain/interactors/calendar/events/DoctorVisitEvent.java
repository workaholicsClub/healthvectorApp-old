package ru.android.childdiary.domain.interactors.calendar.events;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DoctorVisitEvent extends MasterEvent {
    Long id;

    DoctorVisit doctorVisit;

    Doctor doctor;

    String name;

    Integer durationInMinutes;

    String imageFileName;

    @Builder(toBuilder = true)
    private DoctorVisitEvent(Long masterEventId,
                             EventType eventType,
                             DateTime dateTime,
                             Integer notifyTimeInMinutes,
                             String note,
                             Boolean isDone,
                             Child child,
                             Integer linearGroup,
                             Long id,
                             DoctorVisit doctorVisit,
                             Doctor doctor,
                             String name,
                             Integer durationInMinutes,
                             String imageFileName) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.doctorVisit = doctorVisit;
        this.doctor = doctor;
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.imageFileName = imageFileName;
    }
}
