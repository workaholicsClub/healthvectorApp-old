package ru.android.childdiary.domain.interactors.medical;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.repositories.core.RepeatParametersContainer;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Value
@Builder(toBuilder = true)
public class DoctorVisit implements Serializable, RepeatParametersContainer {
    Long id;

    Child child;

    Doctor doctor;

    RepeatParameters repeatParameters;

    String name;

    Integer durationInMinutes;

    DateTime dateTime;

    DateTime finishDateTime;

    Boolean exported;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;
}
