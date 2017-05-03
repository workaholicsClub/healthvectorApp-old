package ru.android.childdiary.domain.interactors.medical;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Value
@Builder(toBuilder = true)
public class DoctorVisit implements Serializable {
    Long id;

    Child child;

    Doctor doctor;

    String name;

    Integer durationInMinutes;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;
}
