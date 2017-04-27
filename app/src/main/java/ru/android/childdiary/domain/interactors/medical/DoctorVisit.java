package ru.android.childdiary.domain.interactors.medical;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Value
@Builder(toBuilder = true)
public class DoctorVisit {
    Long id;

    Doctor doctor;
}
