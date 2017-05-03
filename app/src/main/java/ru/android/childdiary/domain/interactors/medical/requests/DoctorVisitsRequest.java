package ru.android.childdiary.domain.interactors.medical.requests;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Value
@Builder(toBuilder = true)
public class DoctorVisitsRequest {
    Child child;
    DateTime fromDateTime;
    DateTime toDateTime;
    Doctor doctor;
}
