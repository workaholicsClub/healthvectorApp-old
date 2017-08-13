package ru.android.childdiary.domain.calendar.data;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupItem;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DoctorVisitEvent extends MasterEvent implements ContentObject<DoctorVisitEvent>, LinearGroupItem<DoctorVisitEvent> {
    private static final DoctorVisitEvent NULL = DoctorVisitEvent.builder().build();

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

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull DoctorVisitEvent other) {
        return contentEquals(this, other)
                && ObjectUtils.contentEquals(getDoctor(), other.getDoctor())
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equals(getDurationInMinutes(), other.getDurationInMinutes())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(@NonNull DoctorVisitEvent other) {
        List<LinearGroupFieldType> significantFields = new ArrayList<>(
                MasterEvent.getChangedFields(this, other));

        if (!ObjectUtils.contentEquals(getDoctor(), other.getDoctor())) {
            significantFields.add(LinearGroupFieldType.DOCTOR);
        }

        if (!ObjectUtils.contentEquals(getName(), other.getName())) {
            significantFields.add(LinearGroupFieldType.DOCTOR_VISIT_EVENT_NAME);
        }

        if (!ObjectUtils.equals(getDurationInMinutes(), other.getDurationInMinutes())) {
            significantFields.add(LinearGroupFieldType.DOCTOR_VISIT_EVENT_DURATION_IN_MINUTES);
        }

        return significantFields;
    }
}
