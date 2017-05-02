package ru.android.childdiary.domain.interactors.medical;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Value
@Builder(toBuilder = true)
public class MedicineTaking implements Serializable {
    Long id;

    Medicine medicine;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;
}
